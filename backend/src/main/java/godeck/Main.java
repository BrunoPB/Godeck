package godeck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import godeck.database.DatabaseInicialization;
import godeck.game.GameServerSingleton;
import godeck.queue.QueueSingleton;
import godeck.queue.QueueSystem;
import godeck.utils.Printer;

@SpringBootApplication
public class Main {

	private static void startSingletons() {
		GameServerSingleton.getInstance();
		QueueSingleton.getInstance();
	}

	private static void startDatabases() {
		DatabaseInicialization.initializeGameCharacters();
	}

	private static void runQueueSystem() {
		QueueSystem queueSystem = new QueueSystem();
		queueSystem.start();
	}

	private static void runGodeckServer(String[] args) {
		SpringApplication.run(Main.class, args);
		Printer.godeckStart();
		startSingletons();
		startDatabases();
		runQueueSystem();
		Printer.printInfo("Godeck server started successfully!");
	}

	public static void main(String[] args) {
		runGodeckServer(args);
	}
}
