package godeck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import godeck.components.database.DatabaseInicialization;
import godeck.components.game.GameServerSingleton;
import godeck.components.queue.QueueSingleton;
import godeck.components.queue.QueueSystem;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		GameServerSingleton.getInstance();
		QueueSingleton.getInstance();

		DatabaseInicialization.initializeGameCharacters();
		DatabaseInicialization.test_initializeUser(); // TODO: remove this line

		QueueSystem queueSystem = new QueueSystem();
		queueSystem.start();
	}
}
