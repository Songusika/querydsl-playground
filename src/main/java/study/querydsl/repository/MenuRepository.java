package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.domain.menu.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
