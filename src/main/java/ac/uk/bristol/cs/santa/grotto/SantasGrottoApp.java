package ac.uk.bristol.cs.santa.grotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@PropertySources({
        @PropertySource("classpath:default.properties"),
        @PropertySource(value = "file:${user.home}/.secret.properties", ignoreResourceNotFound = true)
})
@SpringBootApplication
public class SantasGrottoApp {

    public static void main(String[] args) {
        SpringApplication.run(SantasGrottoApp.class, args);
    }

}
