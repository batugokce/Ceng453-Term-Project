package tr.metu.ceng.construction.client.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tr.metu.ceng.construction.client.DTO.TableStateDTO;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.enums.EventType;

public class CheatControllerTest {

    @Test
    void cheatSuccess() {
        GameConstants.USERNAME = "username";
        StartGameController controller = new StartGameController();
        TableStateDTO tableStateDTO = controller.startGame();

        CheatController cheatController = new CheatController();
        TableStateDTO returnedTableStateDTO = cheatController.cheat(tableStateDTO);

        Assertions.assertNotNull(returnedTableStateDTO);
        Assertions.assertEquals(returnedTableStateDTO.getEventType(), EventType.CHEAT);
    }

}
