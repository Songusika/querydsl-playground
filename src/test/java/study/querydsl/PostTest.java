package study.querydsl;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.domain.old.post.QPost.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import study.querydsl.domain.old.post.Comment;
import study.querydsl.domain.old.post.Post;
import study.querydsl.domain.old.post.PostCategory;

//@Commit
@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostTest {

    @PersistenceContext
    private EntityManager em;
    private JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void setUp() {
        this.jpaQueryFactory = new JPAQueryFactory(em);

        final List<Post> posts = IntStream
                .rangeClosed(1, 100)
                .mapToObj(index -> {
                            final Post post = createPost(index);
                            addComment(post, 100 - (index-1));
                            return post;
                        }
                ).toList();

        posts.forEach(post -> em.persist(post));
    }

    private Post createPost(final int index) {
        return new Post(index + "번째 컨텐츠입니다.",
                PostCategory.values()[index % PostCategory.values().length],
                LocalDateTime.now().plusDays(index)
        );
    }

    private void addComment(final Post post, final int commentCount) {
        for (int index = 0; index < commentCount; index++) {
            new Comment("댓글입니다", post);
        }
    }

    @Test
    void 최신일_순으로_3번째_페이지_10개를_가져온다() {
        // given
        final int limit = 10;
        final int offset = 3;

        // when
        final List<Post> found = jpaQueryFactory.selectFrom(post)
                .orderBy(post.createdAt.desc())
                .limit(limit)
                .offset(offset * limit)
                .fetch();

        // then
        assertThat(found)
                .hasSize(10)
                .extracting(Post::getContent)
                .containsExactlyInAnyOrderElementsOf(
                        IntStream.rangeClosed(61, 70)
                                .mapToObj(i -> i + "번째 컨텐츠입니다.")
                                .collect(Collectors.toList())
                );
    }

    @Test
    void 댓글이_많은_순으로_3페이지_10개를_가져온다() {
        // given
        final int limit = 10;
        final int offset = 3;

        // when
        final List<Post> found = jpaQueryFactory.selectFrom(post)
                .orderBy(post.comments.size().desc())
                .limit(limit)
                .offset(offset * limit)
                .fetch();

        // then
        assertThat(found)
                .hasSize(10)
                .extracting(Post::getContent)
                .containsExactlyInAnyOrderElementsOf(
                        IntStream.rangeClosed(31, 40)
                                .mapToObj(i -> i + "번째 컨텐츠입니다.")
                                .collect(Collectors.toList())
                );
    }
}
