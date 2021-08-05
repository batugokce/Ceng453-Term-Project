package tr.metu.ceng.construction.client.controller;

import org.junit.jupiter.api.Test;
import tr.metu.ceng.construction.client.constant.GameConstants;

import static org.junit.jupiter.api.Assertions.*;

public class SaveScoreControllerTest {

    @Test
    void saveScoreSuccess() {
        GameConstants.PLAYER_ID = 1L;
        SaveScoreController controller = new SaveScoreController();
        Boolean response = controller.saveScore(30);

        assertNotNull(response);
        assertTrue(response);
    }

}
