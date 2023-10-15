package study.querydsl;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.domain.old.hello.Hello;
import study.querydsl.domain.old.hello.QHello;

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BasicQuerydslTest {

    @PersistenceContext
    private EntityManager em;

    @Test
    void Hello_엔티티를_조회한다() {
        // given
        final JPAQueryFactory query = new JPAQueryFactory(em);
        final QHello qHello = QHello.hello;
        final var hello = new Hello();
        em.persist(hello);

        // when
        final Hello result = query
                .selectFrom(qHello)
                .fetchOne();

        // then
        assertThat(result).isEqualTo(hello);
    }
}
