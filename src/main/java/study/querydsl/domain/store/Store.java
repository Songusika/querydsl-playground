package study.querydsl.domain.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.querydsl.domain.menu.Menu;
import study.querydsl.domain.order.Order;
import study.querydsl.domain.reivew.Review;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String storeName;

    @ManyToOne(fetch = FetchType.LAZY)
    private StoreCategory storeCategory;

    @OneToMany(mappedBy = "store")
    private final List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private final List<Menu> menus = new ArrayList<>();

    public Store(final String storeName, final StoreCategory storeCategory) {
        this.storeName = storeName;
        this.storeCategory = storeCategory;
    }

    public void addMenu(final Menu menu) {
        if(!this.menus.contains(menu)) {
            this.menus.add(menu);
        }
    }

    public void addOrder(final Order order) {
        if(!this.orders.contains(order)) {
            this.orders.add(order);
        }
    }
}
