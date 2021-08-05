package tr.metu.ceng.construction.client.controller;

import org.junit.jupiter.api.Test;
import tr.metu.ceng.construction.client.DTO.TableStateDTO;
import tr.metu.ceng.construction.client.constant.GameConstants;

import static org.junit.jupiter.api.Assertions.*;

public class StartGameTests {

    @Test
    void testStartGame() {
        GameConstants.USERNAME = "username";
        StartGameController controller = new StartGameController();
        TableStateDTO tableStateDTO = controller.startGame();

        assertNotNull(tableStateDTO);
        assertEquals(1, tableStateDTO.getLevel());
    }
}
