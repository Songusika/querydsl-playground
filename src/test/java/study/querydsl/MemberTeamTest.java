package study.querydsl;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static study.querydsl.domain.old.member.QMember.member;
import static study.querydsl.domain.old.member.QTeam.team;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transactional;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.domain.old.member.Member;
import study.querydsl.domain.old.member.QMember;
import study.querydsl.domain.old.member.Team;

//@Commit
@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MemberTeamTest {

    @PersistenceContext
    private EntityManager em;
    private JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void setUp() {
        this.jpaQueryFactory = new JPAQueryFactory(em);
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");

        final Member memberA = new Member("memberA", 10, teamA);
        final Member memberB = new Member("memberB", 20, teamA);
        final Member memberC = new Member("memberC", 30, teamB);
        final Member memberD = new Member("memberD", 40, teamB);

        em.persist(teamA);
        em.persist(teamB);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);

        em.flush();
        em.clear();
    }

    @Test
    void jpql로_멤버를_단건_조회한다() {
        // given
        final String jpql = "SELECT m FROM Member m WHERE m.username = :username";

        // when
        Member found = em.createQuery(jpql, Member.class)
                .setParameter("username", "memberA")
                .getSingleResult();

        // then
        assertThat(found.getUsername()).isEqualTo("memberA");
    }

    @Test
    void queryDsl로_멤버를_단건_조회한다() {
        // given
        QMember qMember = member; // 프로덕션 코드에 사용할 때는 static import 를 해서 사용하자.

        // when
        Member found = jpaQueryFactory
                .select(qMember) //selectFrom 메서드로 더 줄여서 사용이 가능하다.
                .from(qMember)   //selectFrom 이후에 와도 문제가 발생하지 않는다.
                .where(qMember.username.eq("memberA"))
                .fetchOne();

        // then
        assertThat(found.getUsername()).isEqualTo("memberA");
    }

    @Test
    void 이름과_나이가_조건에_맞는_멤버를_조회한다() {
        // given
        final String username = "memberA";
        final int age = 10;

        // when
        Member found = jpaQueryFactory
                .selectFrom(member)
                .where(member.username.eq(username)
                        .and(member.age.eq(age)))
                .fetchOne();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(found.getUsername()).isEqualTo("memberA");
            softly.assertThat(found.getAge()).isEqualTo(10);
        });
    }

    @Test
    void query_dsl_의_where_메서드는_가변인자를_and_조건으로_처리한다() {
        // given
        final String username = "memberA";
        final int age = 10;

        // when
        Member found = jpaQueryFactory
                .selectFrom(member)
                .where(member.username.eq(username),
                        member.age.eq(age))
                .fetchOne();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(found.getUsername()).isEqualTo("memberA");
            softly.assertThat(found.getAge()).isEqualTo(10);
        });
    }

    @Test
    void 이름이_member로_시작하는_모든_멤버를_조회한다() {
        //given
        final String username = "member";

        // when
        final List<Member> all = jpaQueryFactory
                .selectFrom(member)
                .where(member.username.startsWith(username))
                .fetch();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(all).hasSize(4);
            softly.assertThat(all)
                    .allMatch((foundMember) -> foundMember.getUsername().startsWith(username));
        });
    }

    @Test
    void 멤버를_단일_조회_시_결과가_두_개_이상이면_예외가_발생한다() {
        // given
        final String username = "member";

        // expected
        assertThatThrownBy(() -> jpaQueryFactory
                .selectFrom(member)
                .where(member.username.startsWith(username))
                .fetchOne()
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    void 하나의_멤버만_조회한다() {
        final Member found = jpaQueryFactory
                .selectFrom(member)
                .fetchFirst();

        assertThat(found).isNotNull();
    }

    @Test
    void 나이가_10살_초과인_전체_멤버_수를_조회한다() {
        /*
         * fetchCount() 메서드는 deprecated 되었다.
         * fetchCount() 메서드는 개발자가 작성한 Select 쿼리를 기반으로 count 용 쿼리를 내부에서 만들어서 실행한다.
         * 단순한 쿼리에서는 잘 동작하지만 복잡한 쿼리에서는 런타임에러가 발생한다.
         * 때문에 아래와 같은 방법으로 직접 조회하자.
         * fetchResults() 도 같은 이유로 deprecated 되었다.
         * */
        final Long count = jpaQueryFactory
                .select(Wildcard.count) //Wildcard.count 를 사용해도 된다.
                .from(member)
                .where(member.age.gt(10))
                .fetchOne();

        assertThat(count).isEqualTo(3);
    }

    @Test
    void 나이가_20에서_40살_사이의_멤버를_조회한다() {
        // given
        final int startAge = 20;
        final int endAge = 40;

        // when
        final List<Member> found = jpaQueryFactory
                .selectFrom(member)
                .where(member.age.between(startAge, endAge))
                .fetch();

        // then
        assertThat(found).hasSize(3);
    }

    @Test
    void 이름이_memberA가_아닌_멤버를_전부_조회한다() {
        // given
        final String avoidUsername = "memberA";

        // when
        final List<Member> found = jpaQueryFactory
                .selectFrom(member)
                .where(member.username.ne(avoidUsername))
                .fetch();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(found.size()).isEqualTo(3);
            softly.assertThat(found).allMatch(member -> !member.getUsername().equals(avoidUsername));
        });
    }

    @Test
    void 나이가_90살_이상_멤버_중_나이는_내림차순_이름을_오름차순으로_이름이_없다면_맨_마지막으로_조회한다() {
        // given
        em.persist(new Member("memberE", 100));
        em.persist(new Member("memberF", 100));
        em.persist(new Member(null, 100));
        em.persist(new Member("memberG", 99));
        em.persist(new Member(null, 99));

        // when
        final List<Member> found = jpaQueryFactory
                .selectFrom(member)
                .orderBy(member.age.desc().nullsLast(), member.username.asc().nullsLast())
                .fetch();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(found.get(0).getUsername()).isEqualTo("memberE");
            softly.assertThat(found.get(1).getUsername()).isEqualTo("memberF");
            softly.assertThat(found.get(2).getUsername()).isNull();
            softly.assertThat(found.get(3).getUsername()).isEqualTo("memberG");
            softly.assertThat(found.get(4).getUsername()).isNull();
        });
    }

    @Test
    void 멤버의_총합과_나이에_대한_평균_최소_최대를_조회한다() {
        Tuple tuple = jpaQueryFactory
                .select(
                        member.count(),
                        member.age.avg(),
                        member.age.min(),
                        member.age.max()
                )
                .from(member)
                .fetchOne();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(tuple.get(member.count())).isEqualTo(4);
            softly.assertThat(tuple.get(member.age.avg())).isEqualTo(25);
            softly.assertThat(tuple.get(member.age.min())).isEqualTo(10);
            softly.assertThat(tuple.get(member.age.max())).isEqualTo(40);
        });
    }

    @Test
    void 팀의_이름과_팀에_속한_멤버의_평균_나이를_조회한다() {
        List<Tuple> found = jpaQueryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(found.get(0).get(team.name)).isEqualTo("teamA");
            softly.assertThat(found.get(0).get(member.age.avg())).isEqualTo(15);
            softly.assertThat(found.get(1).get(team.name)).isEqualTo("teamB");
            softly.assertThat(found.get(1).get(member.age.avg())).isEqualTo(35);
        });
    }

    @Test
    void 팀A에_소속된_모든_멤버를_조회힌다() {
        List<Member> found = jpaQueryFactory.selectFrom(member)
                .where(member.team.name.eq("teamA"))
                .fetch();

        assertThat(found)
                .map(Member::getTeam)
                .extracting("name")
                .allMatch(teamName -> teamName.equals("teamA"));
    }

    @Test
    void 조인을_통해_팀A에_소속된_모든_멤버를_조회힌다() {
        List<Member> found = jpaQueryFactory.selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(found)
                .map(Member::getTeam)
                .extracting("name")
                .allMatch(teamName -> teamName.equals("teamA"));
    }

    @Test
    void 멤버의_이름이_팀_이름과_같은_멤버를_조회한다() {
        // given
        em.persist(new Member("teamA", 10));
        em.persist(new Member("teamB", 10));

        // when
        final List<Member> found = jpaQueryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        // then
        assertThat(found)
                .map(Member::getUsername)
                .containsExactly("teamA", "teamB");
    }

    @Test
    void 이름이_teamA인_팀과_모든_회원을_조회한다() {
        // given
        final String teamName = "teamA";

        // when
        List<Tuple> found = jpaQueryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq(teamName))
                .fetch();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(found.size()).isEqualTo(4);
            softly.assertThat(found)
                    .elements(0, 1)
                    .map(it -> it.get(team))
                    .extracting("name")
                    .containsOnly("teamA");
            softly.assertThat(found)
                    .elements(2, 3)
                    .map(it -> it.get(team))
                    .containsNull();
            softly.assertThat(found)
                    .map(it -> it.get(member))
                    .extracting("username")
                    .containsExactly("memberA", "memberB", "memberC", "memberD");
        });
    }

    @Test
    void 회원과_이름이_같은_팀과_모든_회원을_조회한다() {
        // given
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        // when
        final List<Tuple> found = jpaQueryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .fetch();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(found.size()).isEqualTo(7);
            softly.assertThat(found)
                    .elements(0, 1, 2, 3, 6)
                    .map(it -> it.get(team))
                    .containsNull();
            softly.assertThat(found)
                    .elements(4, 5)
                    .map(it -> it.get(team))
                    .extracting("name")
                    .containsExactly("teamA", "teamB");
        });
    }

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    void 한번에_멤버와_팀_모두_조회한다() {
        // given
        em.flush();
        em.clear();
        final String username = "memberA";

        // when
        final Member found = jpaQueryFactory
                .selectFrom(member)
                .join(member.team, team)
                .fetchJoin()
                .where(member.username.eq(username))
                .fetchOne();

        // then
        assertThat(emf.getPersistenceUnitUtil().isLoaded(found.getTeam())).isTrue();
    }

    @Test
    void 나이가_평균보다_이상인_멤버를_조회한다() {
        // given
        final QMember subQMember = new QMember("subMember");

        // when
        final List<Member> found = jpaQueryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(subQMember.age.avg())
                                .from(subQMember)
                ))
                .fetch();

        // then
        assertThat(found)
                .map(Member::getUsername)
                .containsExactly("memberC", "memberD");
    }

    @Test
    void query_dsl은_1차캐시를_활용하나() {
        // given
        em.persist(new Member("somsom"));
        em.flush();
        em.clear();

        // when
        final Member somsom = jpaQueryFactory
                .selectFrom(member)
                .where(member.id.eq(5L))
                .fetchOne();

        // then
        assertThat(emf.getPersistenceUnitUtil().isLoaded(somsom)).isTrue();
    }
}
