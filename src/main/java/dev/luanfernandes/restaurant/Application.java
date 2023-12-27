package dev.luanfernandes.restaurant;

import static org.springframework.boot.SpringApplication.run;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@EnableJpaAuditing
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        run(Application.class, args);
    }
}
