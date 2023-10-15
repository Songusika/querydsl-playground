package study.querydsl.domain.old.member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private int age;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(final String username) {
        this(username, 0);
    }

    public Member(final String username, final int age) {
        this(username, age, null);
    }

    public Member(final String username, final int age, final Team team) {
        this.username = username;
        this.age = age;
        this.team = team;
    }

    public void changeTeam(final Team team) {
         if(this.team != null) {
             this.team.removeMember(this);
         }
         this.team = team;
         this.team.addMember(this);
    }
}
