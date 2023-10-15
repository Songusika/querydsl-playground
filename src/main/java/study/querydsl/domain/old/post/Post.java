package study.querydsl.domain.old.post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Enumerated(value = EnumType.STRING)
    private PostCategory postCategory;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    private LocalDateTime createdAt;

    public Post(final String content, final PostCategory postCategory, final LocalDateTime createdAt) {
        this.content = content;
        this.postCategory = postCategory;
        this.createdAt = createdAt;
    }

    public void addComment(final Comment comment) {
        if(!comments.contains(comment)) {
            this.comments.add(comment);
        }
    }
}
