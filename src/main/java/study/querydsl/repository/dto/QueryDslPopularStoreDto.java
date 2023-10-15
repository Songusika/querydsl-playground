package study.querydsl.repository.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import study.querydsl.domain.store.Store;

@Getter
@NoArgsConstructor
public class QueryDslPopularStoreDto {

    private Store store;
    private int totalOrderedMenuCount;
    private int totalReviewScore;

}
