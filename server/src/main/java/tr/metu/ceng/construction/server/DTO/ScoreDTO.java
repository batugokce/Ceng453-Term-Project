package tr.metu.ceng.construction.server.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an overall score for a finished game.
 * It has username of the corresponding player, a score and created date.
 * It will be used for transferring a {@code Score} entity in a simpler format to the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {

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
}
