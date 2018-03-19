package application.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Main entry point for Spring Boot Application
 */
@SpringBootApplication
@EntityScan(basePackages = "models.entities")
public class Application {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
