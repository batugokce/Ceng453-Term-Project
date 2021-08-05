package tr.metu.ceng.construction.client.scene;

import javafx.scene.layout.*;
import javafx.scene.text.Font;

import tr.metu.ceng.construction.client.scene.utils.CommonItems;

/**
 * Responsible for preparing a scene of login
 */
public class LoginScene {

    private static final Font buttonFont = new Font(14);
    private static final String title = "Login";
    private static final int TITLE_FONT_SIZE = 20;
    private static final int LABEL_FONT_SIZE = 15;

    /**
     * Prepares a pane as a root element for login scene
     *
     * @return a pane to use in that scene
     */
    public static Pane draw() {
        return CommonItems.createAuthenticationScene(title, TITLE_FONT_SIZE, LABEL_FONT_SIZE, buttonFont, false);
    }
}
