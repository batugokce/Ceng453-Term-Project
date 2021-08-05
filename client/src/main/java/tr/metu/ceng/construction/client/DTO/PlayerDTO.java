package tr.metu.ceng.construction.client.DTO;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a player.
 * A player has a unique username and a password.
 */
@Getter
@Setter
public class PlayerDTO{

    /**
     * Indicates the username of the player.
     * It should be unique.
     */
    private String username;

    /**
     * Indicates the password of the player.
     */
    private String password;

    /**
     * Indicates the email address of the player.
     */
    private String emailAddress;

}
