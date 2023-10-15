package study.querydsl.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.domain.order.Order;
import study.querydsl.domain.reivew.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByOrder(final Order order);
}
