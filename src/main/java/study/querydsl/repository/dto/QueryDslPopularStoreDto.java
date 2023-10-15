package study.querydsl.repository.dto;

import lombok.NoArgsConstructor;
import study.querydsl.domain.store.Store;

@NoArgsConstructor
public class QueryDslPopularStoreDto {

    private Store store;
    private int totalMenuCount;
    private int totalReviewScore;

}
