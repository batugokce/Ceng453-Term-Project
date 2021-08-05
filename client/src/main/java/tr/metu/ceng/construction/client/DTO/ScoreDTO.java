package tr.metu.ceng.construction.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an overall score for a finished game.
 * It has username of the corresponding player, a score and created date.
 * It will be used for listing the scores in the leaderboard table.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {

    /**
     * Indicates the index of the row of the object.
     */
    private int number;

    /**
     * Indicates the username of the player scored it.
     */
    private String username;

    /**
     * Indicates the score(point).
     */
    private int score;

    /**
     * Indicates the date when the score recorded.
     */
    private String createdDate;

    @Override
    public String toString() {
        return "ScoreDTO{" +
                "username='" + username + '\'' +
                ", score=" + score +
                ", createdDate=" + createdDate +
                '}';
    }
}
