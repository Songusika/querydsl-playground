package study.querydsl.domain.old.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public Team(final String name) {
        this.name = name;
    }

    public void addMember(final Member member) {
        if (!members.contains(member)) {
            this.members.add(member);
        }
    }

    public void removeMember(final Member member) {
        members.remove(member);
    }
}
