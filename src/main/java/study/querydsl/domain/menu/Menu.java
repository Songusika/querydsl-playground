package study.querydsl.domain.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.querydsl.domain.store.Store;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private long price;

    @Column
    private long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    public Menu(final String name, final long price, final Store store) {
        this.name = name;
        this.price = price;
        this.store = store;
        store.addMenu(this);
        this.likeCount = 0;
    }

    public void updateLikeCount() {
        this.likeCount++;
    }
}
