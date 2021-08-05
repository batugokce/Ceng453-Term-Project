package tr.metu.ceng.construction.client;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tr.metu.ceng.construction.client.constant.StageConstants;
import tr.metu.ceng.construction.client.scene.MainMenuScene;


import static tr.metu.ceng.construction.client.UiApplication.StageReadyEvent;

/**
 * Responsible for initializing the stage.
 * Listens for 'StageReadyEvent'. When that event is published, stage is initialized.
 */
@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Value("${spring.application.ui.windowWidth}")
    private int windowWidth;
    @Value("${spring.application.ui.windowHeight}")
    private int windowHeight;
    @Value("${spring.application.ui.title}")
    private String title;

    /**
     * Triggered when 'StageReadyEvent' is published.
     * @param stageReadyEvent holds information of the event
     */
    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        Stage stage = stageReadyEvent.getStage();
        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(new Scene(MainMenuScene.draw()));
        StageConstants.stage = stage;
        StageConstants.title = title;
        stage.show();
    }

}
