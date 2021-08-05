package tr.metu.ceng.construction.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tr.metu.ceng.construction.server.DTO.LoginCredentialsDTO;
import tr.metu.ceng.construction.server.config.Encryption;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.repository.PlayerRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository repository;
    @Mock
    private Encryption encryption;
    @InjectMocks
    private PlayerService service;

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setId(1L);
        player.setUsername("username");
        player.setPassword("pass");
        player.setEmailAddress("xyz@xyz.com");
    }

    @Test
    void findByUsernameTest() {
        when(repository.findByUsername(anyString())).thenReturn(player);

        Player playerDB = service.findByUsername("username");

        assertEquals(player, playerDB);
    }

    @Test
    void createPlayerTestAlreadyExistingUsername() {
        when(repository.existsByUsername(anyString())).thenReturn(true);

        Player playerDB = service.createPlayer(player);

        assertNull(playerDB);
    }

    @Test
    void createPlayerTestAlreadyExistingEmailAddress() {
        when(repository.existsByEmailAddress(anyString())).thenReturn(true);

        Player playerDB = service.createPlayer(player);

        assertNull(playerDB);
    }

    @Test
    void createPlayerTestSuccess() {
        when(encryption.encoder()).thenReturn(new BCryptPasswordEncoder());
        when(repository.existsByUsername(anyString())).thenReturn(false);
        when(repository.save(any())).thenReturn(player);

        Player playerDB = service.createPlayer(player);

        assertNotNull(playerDB);
        verify(repository).save(any());
    }

    @Test
    void loginTestIncorrectPassword() {
        when(encryption.encoder()).thenReturn(new BCryptPasswordEncoder());
        LoginCredentialsDTO loginCredentials = new LoginCredentialsDTO("username","incorrectPass","", 1L);
        when(repository.findByUsername(anyString())).thenReturn(player);

        LoginCredentialsDTO loginResponse = service.login(loginCredentials);

        assertNull(loginResponse.getToken());
    }

    @Test
    void loginTestCorrectPassword() {
        when(encryption.encoder()).thenReturn(new BCryptPasswordEncoder());
        LoginCredentialsDTO loginCredentials = new LoginCredentialsDTO("username","pass","", 1L);
        player.setPassword(new BCryptPasswordEncoder().encode("pass"));
        when(repository.findByUsername(anyString())).thenReturn(player);

        LoginCredentialsDTO loginResponse = service.login(loginCredentials);

        assertNotNull(loginResponse.getToken());
    }
}