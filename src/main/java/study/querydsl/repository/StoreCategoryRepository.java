package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.domain.store.StoreCategory;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {
}
