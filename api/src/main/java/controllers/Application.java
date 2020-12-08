package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.SQLException;

@SpringBootApplication
public class Application {
    protected ConnectionSource connectionSource;

    public Application() throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:mariadb://bilbao.informatik" +
                ".uni-stuttgart.de/team12", "team12", "e345c973d6f4d770");
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // enable cross-origin resource sharing (CORS)
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // allow CORS requests for all resources and HTTP methods from the frontend origin
                registry.addMapping("/**")
                        .allowedMethods("OPTIONS", "HEAD", "GET", "PUT", "POST", "DELETE");
                //.allowedOrigins("http://localhost:8000");
            }
        };
    }



}
