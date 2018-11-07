package ac.uk.bristol.cs.santa.grotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("file:${user.home}/secret.properties")
@SpringBootApplication
public class SantasGrottoApp {

    public static void main(String[] args) {
        SpringApplication.run(SantasGrottoApp.class, args);
    }

}
