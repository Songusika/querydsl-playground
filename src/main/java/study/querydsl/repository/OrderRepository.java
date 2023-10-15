package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.domain.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
