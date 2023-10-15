package study.querydsl.repository.dto;

import study.querydsl.domain.order.Order;

public interface JPQLPopularStoreDto {

    Order getOrder();
    int getTotalMenuCount();
    int getTotalReviewStore();
}
