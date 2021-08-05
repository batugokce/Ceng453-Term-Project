package tr.metu.ceng.construction.client.controller;

import org.junit.jupiter.api.Test;
import tr.metu.ceng.construction.client.controller.RegisterController;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTests {

    @Test
    void testRegister() {
        RegisterController registerController = new RegisterController();
        String response = registerController.register("username5", "password", "test@test.com");

        assertNotNull(response);
    }

}
