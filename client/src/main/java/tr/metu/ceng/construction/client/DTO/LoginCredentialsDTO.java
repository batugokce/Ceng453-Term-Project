package tr.metu.ceng.construction.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Responsible for authentication functionalities.
 * When a user wants to login, this DTO will be used to hold credentials.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginCredentialsDTO {

    private String username;
    private String password;
    private String token;
    private Long playerId;

}
