package tr.metu.ceng.construction.client.ui;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import tr.metu.ceng.construction.client.UiApplication;
import tr.metu.ceng.construction.client.component.FaceDownCardBox;
import tr.metu.ceng.construction.client.component.LeftMenuComponent;
import tr.metu.ceng.construction.client.component.LeftMenuForMultiplayer;
import tr.metu.ceng.construction.client.constant.GameConstants;
import tr.metu.ceng.construction.client.enums.EventType;
import tr.metu.ceng.construction.client.scene.*;
import tr.metu.ceng.construction.common.MultiplayerEvent;
import tr.metu.ceng.construction.common.PlayerType;
import tr.metu.ceng.construction.common.TableStateForMultiplayer;

import static org.junit.jupiter.api.Assertions.*;
import static tr.metu.ceng.construction.client.constant.RequestConstants.WEEKLY;


public class UITests extends ApplicationTest {

    @Test
    public void testMainMenu() {
        Pane root = MainMenuScene.draw();

        assertEquals(5, root.getChildren().size());
    }

    @Test
    public void testLoginScene() {
        Pane root = LoginScene.draw();

        assertEquals(4, root.getChildren().size());
    }

    @Test
    public void testRegisterScene() {
        Pane root = RegisterScene.draw();

        assertEquals(4, root.getChildren().size());
    }

    @Test
    public void testLeaderboardScene() {
        Pane root = LeaderboardScene.start();

        assertEquals(3, root.getChildren().size());
    }

    @Test
    public void testLoggedInScene() {
        Pane root = MainMenuScene.createStartScene();

        assertEquals(4, root.getChildren().size());
    }

    @Test
    public void testLeftMenuComponent() {
        LeftMenuComponent leftMenuComponent = new LeftMenuComponent(1,0,0,0,0,0, EventType.START_GAME, 1);
        LeftMenuComponent newLeftMenuComponent = new LeftMenuComponent(1,5,0,3,0,0, EventType.PLAYER1_CAPTURED, 1);
        GameConstants.faceDownCardBox = new FaceDownCardBox(40);

        leftMenuComponent.setAll(newLeftMenuComponent, false);
    }

    @Test
    public void testLeftMenuForMultiPlayerComponent() {
        LeftMenuForMultiplayer leftMenuComponent = new LeftMenuForMultiplayer(1,0,0,0,0,
                0, MultiplayerEvent.PLAYER1_CAPTURED, 2);
        LeftMenuForMultiplayer newLeftMenuComponent = new LeftMenuForMultiplayer(1,5,0,3,0,
                0, MultiplayerEvent.PLAYER2_WIN, 1);
        GameConstants.faceDownCardBox = new FaceDownCardBox(40);

        leftMenuComponent.setAll(newLeftMenuComponent);
        assertEquals(1, leftMenuComponent.getTurn());
    }

    @Test
    public void testForgotPasswordScene() {
        Pane root = ForgotPasswordScene.draw();

        assertEquals(3, root.getChildren().size());
    }

    @Test
    public void testGameEndScene() {
        TableStateForMultiplayer tableStateForMultiplayer = new TableStateForMultiplayer();
        tableStateForMultiplayer.setEventType(MultiplayerEvent.PLAYER1_WIN);
        tableStateForMultiplayer.setPlayer1Username("user");
        tableStateForMultiplayer.setPlayer1LevelScore(151);
        tableStateForMultiplayer.setPlayer2LevelScore(20);
        tableStateForMultiplayer.setPlayer1CumulativeScore(150);
        tableStateForMultiplayer.setPlayer2CumulativeScore(250);
        GameConstants.WHICH_PLAYER_YOU_ARE = PlayerType.PLAYER1;

        Pane root = GameEndScene.draw(tableStateForMultiplayer);

        assertEquals(4, root.getChildren().size());
    }

    @Test
    public void testMatchMakingScene() {
        Pane root = MatchMakingScene.drawMatchingScene();

        assertEquals(2, root.getChildren().size());
    }

    @Test
    public void testMatchMadeScene() {
        String player2Username = "user";
        Pane root = MatchMakingScene.drawMatchedScene(player2Username);

        assertEquals(1, root.getChildren().size());
    }

}
