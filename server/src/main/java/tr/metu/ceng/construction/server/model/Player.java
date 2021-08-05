package tr.metu.ceng.construction.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.server.common.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a player.
 * A player has a unique username and a password.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Player extends BaseEntity {

    /**
     * Holds a reference to the game where the player entity plays.
     * It may be null if the game has not started yet.
     */
    @OneToOne
    private Game game;

    /**
     * Indicates the username of the player.
     * It should be unique.
     */
    @Column(name="USERNAME", unique = true)
    private String username;

    /**
     * Indicates the password of the player.
     * It should be kept in encrypted form.
     */
    @Column(name="PASSWORD")
    private String password;

    /**
     * Indicates the email address of the player.
     */
    @Column(name="EMAIL", unique = true)
    private String emailAddress;

    /**
     * Holds token of the player
     * This attribute will be set whenever the player logs in.
     */
    @Column(name="TOKEN")
    private String token;

    /**
     * Indicates the cumulative(total) score achieved by the player up to the current level.
     * It should be zero at the first level.
     * When user levels up, level score will be added to this to update cumulative score.
     */
    @Column(name="CUMULATIVE_SCORE")
    private Integer cumulativeScore;

    /**
     * Indicates token required for password reset operation.
     * When a user wants to reset his password, this token is generated randomly and sent to his e-mail.
     */
    @Column(name = "PASSWORD_RESET_TOKEN")
    private String passwordResetToken;

    /**
     * Holds the list of the scores that belong to the player for all of his finished games.
     */
    @OneToMany(mappedBy = "ownerPlayer", cascade = CascadeType.ALL)
    private List<Score> overallScores = new ArrayList<>();


    /**
     * This method checks whether a given object is equals to this player or not.
     * If id and username of two players are equal, these two players are identical.
     *
     * @param object this is the object to check equality.
     * @return a boolean that indicates if two objects are equal or not.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Player)) return false;
        Player player = (Player) object;
        return getId().equals(player.getId()) &&
                username.equals(player.username);
    }

    /**
     * Returns a hash code for this {@code Player}.
     *
     * @return a hash code value for this object based on id and username.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), username);
    }
}
