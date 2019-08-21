package techcourse.fakebook.domain.friendship;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.exception.FriendshipNotRelatedUserIdException;
import techcourse.fakebook.exception.InvalidFriendshipUserIdException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "precedent_user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User precedentUser;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Friendship() {
    }

    public Friendship(User precedentUser, User user) {
        validatePrecedentUserIdIsLessThanUserId(precedentUser, user);

        this.precedentUser = precedentUser;
        this.user = user;
    }

    private void validatePrecedentUserIdIsLessThanUserId(User precedentUser, User user) {
         if (precedentUser.getId() >= user.getId()) {
             throw new InvalidFriendshipUserIdException();
         }
    }

    public static Friendship from(User precedentUser, User user) {
        if (precedentUser.getId() >= user.getId()) {
            // swap
            User tmp = precedentUser;
            precedentUser = user;
            user = tmp;
        }
        return new Friendship(precedentUser, user);
    }

    public Long getFriendId(Long myId) {
        if (!myId.equals(precedentUser.getId())
                && !myId.equals(user.getId())) {
            throw new FriendshipNotRelatedUserIdException();
        }
        return myId.equals(precedentUser.getId()) ? user.getId() : precedentUser.getId();
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", precedentUser=" + precedentUser +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(precedentUser, that.precedentUser) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, precedentUser, user);
    }
}