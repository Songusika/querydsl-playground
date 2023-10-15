package study.querydsl.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.domain.store.Store;
import study.querydsl.domain.store.StoreCategory;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByStoreCategory(final StoreCategory storeCategory);
}
