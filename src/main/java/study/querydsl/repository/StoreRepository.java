package study.querydsl.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.querydsl.domain.store.Store;
import study.querydsl.domain.store.StoreCategory;

public interface StoreRepository extends JpaRepository<Store, Long>, CustomStoreRepository {

    List<Store> findByStoreCategory(final StoreCategory storeCategory);

    @Query("SELECT s as store, " +
        "COALESCE(SUM(om.quantity), 0) AS totalMenuCount, " +
        "COALESCE(SUM(r.rate), 0) AS totalReviewScore " +
        "FROM Store s " +
        "LEFT JOIN s.orders o " +
        "LEFT JOIN o.orderMenus om " +
        "LEFT JOIN o.review r " +
        "WHERE s.id <> :storeId " +
        "AND s.storeCategory = (SELECT s1.storeCategory FROM Store s1 WHERE s1.id = :storeId) " +
        "GROUP BY s.id " +
        "HAVING SUM(om.quantity) >= (SELECT SUM(om1.quantity) FROM OrderMenu om1 WHERE om1.order.store.id = :storeId) " +
        "AND (SUM(r.rate) > (SELECT SUM(r1.rate) FROM Review r1 WHERE r1.order.store.id = :storeId) " +
        "OR (SUM(r.rate) = (SELECT SUM(r2.rate) FROM Review r2 WHERE r2.order.store.id = :storeId) AND s.id > :storeId)) " +
        "ORDER BY COALESCE(SUM(r.rate), 0) DESC")
    List<Store> findPopularStoreThanWithJPQL(@Param("storeId") final long id);
}
