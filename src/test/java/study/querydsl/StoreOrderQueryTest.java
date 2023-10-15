package study.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import study.querydsl.domain.menu.Menu;
import study.querydsl.domain.order.Order;
import study.querydsl.domain.order.OrderMenu;
import study.querydsl.domain.reivew.Review;
import study.querydsl.domain.store.Store;
import study.querydsl.domain.store.StoreCategory;
import study.querydsl.repository.MenuRepository;
import study.querydsl.repository.OrderRepository;
import study.querydsl.repository.ReviewRepository;
import study.querydsl.repository.StoreCategoryRepository;
import study.querydsl.repository.StoreRepository;

//@Commit
@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class StoreOrderQueryTest {

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ReviewRepository repository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private StoreCategory 일식;
    private StoreCategory 한식;

    @BeforeEach
    void setUp() {
        카테고리를_등록한다();
        가게를_등록_한다();
        모든_가게에_메뉴를_등록_한다();
        모든_가게의_모든_메뉴에_주문을_등록_한다();
        모든_가게의_주문에_리뷰룰_작성한다();
    }

    private void 카테고리를_등록한다() {
        this.일식 = storeCategoryRepository.save(new StoreCategory("일식"));
        this.한식 = storeCategoryRepository.save(new StoreCategory("한식"));
    }

    private void 가게를_등록_한다() {
        IntStream.rangeClosed(1, 5).forEach(index -> {
            storeRepository.save(new Store(index + "번째" + 일식.getCategory() + "가게", 일식));
            storeRepository.save(new Store(index + "번째" + 한식.getCategory() + "가게", 한식));
        });
    }

    private void 모든_가게에_메뉴를_등록_한다() {
        final List<Store> 일식_가게들 = storeRepository.findByStoreCategory(일식);
        final List<Store> 한식_가게들 = storeRepository.findByStoreCategory(한식);

        일식_가게들.forEach(일식_가게 -> {
            하나의_가게에_메뉴를_추가_한다(일식_가게, 5);
        });
        한식_가게들.forEach(한식_가게 -> {
            하나의_가게에_메뉴를_추가_한다(한식_가게, 5);
        });
    }

    private void 하나의_가게에_메뉴를_추가_한다(final Store store, final int menuCount) {
        for (int index = 0; index < menuCount; index++) {
            final String 카테고리 = store.getStoreCategory().getCategory();
            menuRepository.save(new Menu(" " + index + 카테고리 + "맛탕", 1000L + index, store));
        }
    }

    private void 모든_가게의_모든_메뉴에_주문을_등록_한다() {
        final List<Store> 일식_가게들 = storeRepository.findByStoreCategory(일식);
        final List<Store> 한식_가게들 = storeRepository.findByStoreCategory(한식);

        일식_가게들.forEach(일식_가게 -> {
            하나의_가게에_주문을_한다(일식_가게, 2);
        });
        한식_가게들.forEach(한식_가게 -> {
            하나의_가게에_주문을_한다(한식_가게, 2);
        });
    }

    private void 하나의_가게에_주문을_한다(final Store 가게, final int 주문_개수) {
        for (int index = 0; index < 주문_개수; index++) {
            final Order 주문 = (new Order(가게));
            하나의_주문에_주문_메뉴를_추가한다(주문, 2);
            orderRepository.save(주문);
        }
    }

    private void 하나의_주문에_주문_메뉴를_추가한다(final Order 주문, final int 주문_메뉴_개수) {
        for (int index = 0; index < 주문_메뉴_개수; index++) {
            final Menu 메뉴 = menuRepository.findById(1L).get();
            final OrderMenu 주문_메뉴 = new OrderMenu(주문, 메뉴);
        }
    }

    private void 모든_가게의_주문에_리뷰룰_작성한다() {
        final List<Store> 일식_가게들 = storeRepository.findByStoreCategory(일식);
        final List<Store> 한식_가게들 = storeRepository.findByStoreCategory(한식);

        일식_가게들.forEach(일식_가게 -> {
            final List<Order> 일식_가게_주문들 = 일식_가게.getOrders();
            가게의_모든_주문에_리뷰를_작성한다(일식_가게_주문들);
        });

        한식_가게들.forEach(한식_가게 -> {
            final List<Order> 한식_가게_주문들 = 한식_가게.getOrders();
            가게의_모든_주문에_리뷰를_작성한다(한식_가게_주문들);
        });
    }

    private void 가게의_모든_주문에_리뷰를_작성한다(final List<Order> 가게_주문들) {
        가게_주문들.forEach(가게_주문 -> {
            final Review 리뷰 = repository.save(new Review("맛있어요", (가게_주문.getId().intValue() % 5) + 1, 가게_주문));
            가게_주문.addReview(리뷰);
        });
    }
}
