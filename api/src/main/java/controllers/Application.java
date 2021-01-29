package controllers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.SQLException;

import static properties.Properties.*;

@OpenAPIDefinition(info = @Info(title = " SoPra WS2021 REST API", version = "1.3",
        description = "The REST API Documentation. Methods to " +
                "create, read, update and delete each entity have been implemented. Many methods have been implemented to manipulate the entities themselves. Others, however, were implemented because they were needed and contributed to the realization of logical relations between entities."))
@SpringBootApplication
public class Application {
    public ConnectionSource connectionSource;

    public Application() throws SQLException {
        /*connectionSource = new JdbcConnectionSource("jdbc:mariadb://bilbao.informatik" +
                ".uni-stuttgart.de/team12", "team12", "e345c973d6f4d770");*/
        connectionSource = new JdbcConnectionSource("jdbc:mariadb://" + LINK, USERNAME, PASSWORD);
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
