package study.querydsl.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.querydsl.domain.store.Store;
import study.querydsl.domain.store.StoreCategory;
import study.querydsl.repository.dto.JPQLPopularStoreDto;

public interface StoreRepository extends JpaRepository<Store, Long>, CustomStoreRepository {

    List<Store> findByStoreCategory(final StoreCategory storeCategory);

    @Query("SELECT s as store, " +
        "COALESCE(SUM(om.quantity), 0) AS totalOrderedMenuCount, " +
        "COALESCE(SUM(r.rate), 0) AS totalReviewScore " +
        "FROM Store s " +
        "LEFT JOIN s.orders o " +
        "LEFT JOIN o.orderMenus om " +
        "LEFT JOIN o.review r " +
        "WHERE s.id <> :storeId " +
        "AND s.storeCategory = (SELECT s1.storeCategory FROM Store s1 WHERE s1.id = :storeId) " +
        "GROUP BY s.id " +
        "HAVING SUM(om.quantity) >= (SELECT SUM(om1.quantity) FROM OrderMenu om1 WHERE om1.order.store.id = :storeId) " +
        // TODO: 2023-10-15 동적쿼리로 들어오는 상황으로 변경하거나 추가하기
        "AND (SUM(r.rate) > (SELECT SUM(o1.review.rate) FROM Order o1 WHERE o1.store.id = :storeId) " +
        "OR (SUM(r.rate) = (SELECT SUM(o2.review.rate) FROM Order o2 WHERE o2.store.id = :storeId) AND s.id > :storeId)) " +
        "ORDER BY COALESCE(SUM(r.rate), 0) DESC")
    List<JPQLPopularStoreDto> findPopularStoreThanWithJPQL(@Param("storeId") final long id);
}
