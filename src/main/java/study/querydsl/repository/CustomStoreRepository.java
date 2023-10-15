package study.querydsl.repository;

import java.util.List;
import study.querydsl.repository.dto.QueryDslPopularStoreDto;

public interface CustomStoreRepository {

    List<QueryDslPopularStoreDto> findPopularStoreThan(final long id);

}
