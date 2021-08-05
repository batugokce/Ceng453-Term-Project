package tr.metu.ceng.construction.server.service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import tr.metu.ceng.construction.server.DTO.LoginCredentialsDTO;
import tr.metu.ceng.construction.server.config.Encryption;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.repository.PlayerRepository;

/**
 * Responsible for account operations such as creation of accounts, login etc.
 */
@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final Encryption encryption;

    /**
     * Given a username, finds and returns the player
     *
     * @param username username of the player
     * @return the player
     */
    public Player findByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    /**
     * Create a new player account in the database.
     *
     * @param player entity to save
     * @return saved player entity
     */
    public Player createPlayer(Player player) {
        // check whether unique player can be created or not
        if (playerRepository.existsByUsername(player.getUsername())) {
            return null;
        }
        if (playerRepository.existsByEmailAddress(player.getEmailAddress())) {
            return null;
        }
        player.setPassword(encryption.encoder().encode(player.getPassword()));
        return playerRepository.save(player);
    }

    /**
     * Responsible for authentication operations.
     * When a user logs in, a token is generated randomly and returned to the client.
     * User will use that token in his/her next requests.
     *
     * @param loginCredentialsDTO DTO that includes username and password
     * @return a DTO that includes randomly generated token
     */
    public LoginCredentialsDTO login(LoginCredentialsDTO loginCredentialsDTO) {
        // fetch the player entity from the database
        Player player = playerRepository.findByUsername(loginCredentialsDTO.getUsername());

        if (null == player || !encryption.encoder().matches(loginCredentialsDTO.getPassword(), player.getPassword())) {
            // if credentials mismatch, just returns without generating a token
            return new LoginCredentialsDTO();
        }

        String savedToken = saveToken(player);
        // insert generated token into the DTO and return it as a response to the client
        return new LoginCredentialsDTO(null, null, savedToken, player.getId());
    }

    private String saveToken(Player player) {
        // a random token is generated, and saved into player entity
        String token = RandomString.make(10);
        player.setToken(token);
        playerRepository.save(player);
        return token;
    }
}
