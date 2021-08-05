package tr.metu.ceng.construction.server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.metu.ceng.construction.server.DTO.LoginCredentialsDTO;
import tr.metu.ceng.construction.server.model.Player;
import tr.metu.ceng.construction.server.service.PlayerService;

@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
@RestController
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/create")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player savedPlayer = playerService.createPlayer(player);
        return ResponseEntity.ok(savedPlayer);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginCredentialsDTO> login(@RequestBody LoginCredentialsDTO loginCredentialsDTO) {
        LoginCredentialsDTO loginResponse = playerService.login(loginCredentialsDTO);
        if (null == loginResponse.getToken()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponse);
        }
        return ResponseEntity.ok(loginResponse);
    }
}
