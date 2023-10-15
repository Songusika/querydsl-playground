package study.querydsl.domain.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.querydsl.domain.reivew.Review;
import study.querydsl.domain.store.Store;

@Entity
@Getter
@Table(name = "`order`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String orderStatus;

    @OneToOne
    private Review review;

    @Getter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private final List<OrderMenu> orderMenus = new ArrayList<>();

    @Column
    private LocalDateTime createdAt;

    public Order(final Store store) {
        this.orderStatus = "DONE";
        this.store = store;
        this.createdAt = LocalDateTime.now();
        store.addOrder(this);
    }

    public void addReview(final Review review) {
        if(this.review != null) {
            this.review = review;
        }
    }

    public void addOrderMenu(final OrderMenu orderMenu) {
        if(!orderMenus.contains(orderMenu)) {
            orderMenus.add(orderMenu);
        }
    }
}
