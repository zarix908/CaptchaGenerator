package app.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        new ArgumentsParser(args).parse();
        SpringApplication.run(Application.class, args);
    }
}