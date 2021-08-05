package tr.metu.ceng.construction.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tr.metu.ceng.construction.server.api.MultiPlayerController;

@SpringBootApplication
@EnableSwagger2
public class ServerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startMultiPlayerThread() {
		Thread multiPlayerController = new Thread(new MultiPlayerController());
		multiPlayerController.start();
	}

}
