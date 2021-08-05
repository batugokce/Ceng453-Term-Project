package tr.metu.ceng.construction.client.controller;

import org.junit.jupiter.api.Test;
import tr.metu.ceng.construction.client.controller.LoginController;

import static org.junit.jupiter.api.Assertions.*;
import static tr.metu.ceng.construction.client.constant.MessageConstants.*;

public class LoginTests {

    @Test
    void testSuccessLogin() {
        LoginController loginController = new LoginController();
        String response = loginController.login("doNotDeleteMe", "testpassword");

        assertEquals(SUCCESSFUL_LOGIN, response);
    }

    @Test
    void testFailLogin() {
        LoginController loginController = new LoginController();
        String response = loginController.login("doNotDeleteMe", "incorrectPassword");

        assertEquals(INVALID_LOGIN, response);
    }
}
