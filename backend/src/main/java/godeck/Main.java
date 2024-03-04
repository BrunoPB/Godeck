package godeck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import godeck.components.database.DatabaseInicialization;
import godeck.components.game.GameServerSingleton;
import godeck.components.queue.QueueSingleton;
import godeck.components.queue.QueueSystem;

@SpringBootApplication
public class Main {

	private static void startSingletons() {
		GameServerSingleton.getInstance();
		QueueSingleton.getInstance();
	}

	private static void startDatabases() {
		DatabaseInicialization.initializeGameCharacters();
		DatabaseInicialization.test_initializeUser(); // TODO: remove this line
	}

	private static void runQueueSystem() {
		QueueSystem queueSystem = new QueueSystem();
		queueSystem.start();
	}

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		startSingletons();
		startDatabases();
		runQueueSystem();
	}
}
