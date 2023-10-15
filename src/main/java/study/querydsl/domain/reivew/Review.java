package study.querydsl.domain.reivew;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.querydsl.domain.order.Order;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    private int rate;

    @OneToOne(fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    private Order order;

    @Column
    private LocalDateTime createdAt;

    public Review(final String content, final int rate, final Order order) {
        this.content = content;
        this.order = order;
        this.rate = rate;
        this.createdAt = LocalDateTime.now();
        order.addReview(this);
    }
}
