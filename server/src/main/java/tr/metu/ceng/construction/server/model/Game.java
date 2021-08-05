package tr.metu.ceng.construction.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.metu.ceng.construction.server.common.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

import static tr.metu.ceng.construction.server.common.CommonConstants.BLUFFING_AND_SINGLEPLAYER_GAME_LEVEL;
import static tr.metu.ceng.construction.server.common.CommonConstants.MULTIPLAYER_GAME_LEVEL;

/**
 * Represents a game that is being played between two players or a player and computer.
 * If the level is 4, game has two real players.
 * If level is 1,2 or 3. Then, this game is played between a player and the computer.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Game extends BaseEntity {

    /**
     * Holds a reference to the first player of this game entity.
     * This attribute should not be null.
     */
    @OneToOne
    @JoinColumn(name = "PLAYER1_ID")
    private Player player1;

    /**
     * Holds a reference to the second player of this game entity.
     * If the game is offline, i.e. level is not 4, then this attribute should be null.
     */
    @OneToOne
    @JoinColumn(name = "PLAYER2_ID")
    private Player player2;

    /**
     * Indicates the level of the game. It can take values from 1 to 4.
     */
    @Column(name = "LEVEL")
    private Integer level;

    /**
     * Checks whether the game is online or not.
     *
     * @return a boolean that indicates if the game is multiplayer or not.
     */
    public boolean isMultiPlayer() {
        return this.level.equals(4);
    }

    /**
     * @return if game is bluffing pisti or standard pisti.
     */
    public boolean isBluffingPisti() {
        return level.equals(BLUFFING_AND_SINGLEPLAYER_GAME_LEVEL) || level.equals(MULTIPLAYER_GAME_LEVEL);
    }

    /**
     * This method checks whether a given object is equals to this game or not.
     * If id's, players and levels of two game are same, these two games are identical.
     *
     * @param otherObject this is the object to check equality.
     * @return a boolean that indicates if two objects are equal or not.
     */
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (!(otherObject instanceof Game)) return false;
        Game game = (Game) otherObject;
        return getId().equals(game.getId()) &&
                player1.equals(game.player1) &&
                Objects.equals(player2, game.player2) &&
                Objects.equals(level, game.level);
    }

    /**
     * Returns a hash code for this {@code Game}.
     *
     * @return a hash code value for this object depending on id, players and level.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), player1, player2, level);
    }

}
