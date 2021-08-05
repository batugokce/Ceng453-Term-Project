package tr.metu.ceng.construction.client.controller;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import tr.metu.ceng.construction.client.controller.ForgotPasswordController;

import static org.junit.jupiter.api.Assertions.*;

public class ForgotPasswordTests {

    @Test
    void testForgotPasswordSuccess() {
        ForgotPasswordController controller = new ForgotPasswordController();
        Mono<Boolean> booleanMono = controller.forgotPassword("batuhangokce98@gmail.com");

        Boolean response = booleanMono.block();

        assertNotNull(response);
        assertTrue(response);
    }

    @Test
    void testForgotPasswordFail() {
        ForgotPasswordController controller = new ForgotPasswordController();
        Mono<Boolean> booleanMono = controller.forgotPassword("testabcd@gmail.com");

        Boolean response = booleanMono.block();

        assertNotNull(response);
        assertFalse(response);
    }
}
