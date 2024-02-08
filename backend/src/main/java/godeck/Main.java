package godeck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import godeck.database.DatabaseInicialization;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		DatabaseInicialization.initializeGameCharacters();
		DatabaseInicialization.test_initializeUser();
	}
}
