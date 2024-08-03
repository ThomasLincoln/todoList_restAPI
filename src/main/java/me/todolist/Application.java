package me.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

import me.todolist.Application;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory(System.getProperty("user.dir"))
				.load();
				
		System.setProperty("spring.datasource.url", dotenv.get("DATABASE_URL"));
		System.setProperty("spring.datasource.username", dotenv.get("DATABASE_USERNAME"));
		System.setProperty("spring.datasource.password", dotenv.get("DATABASE_PASSWORD"));
		System.setProperty("spring.profiles.active", dotenv.get("SPRING_PROFILES_ACTIVE"));
		System.setProperty("app.jwtSecret", dotenv.get("JWT_SECRET"));
		System.setProperty("app.jwtExpirationMs", dotenv.get("JWT_EXPIRATION_MS"));

		SpringApplication.run(Application.class, args);

	}

}
