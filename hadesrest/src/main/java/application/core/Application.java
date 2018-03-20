package application.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Main entry point for Spring Boot Application
 */
@SpringBootApplication
@EntityScan(basePackages = "models.entities")
@EnableAutoConfiguration
public class Application {


    
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
