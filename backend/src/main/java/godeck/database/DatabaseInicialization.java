package godeck.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import godeck.models.entities.GameCharacter;
import godeck.models.entities.User;
import godeck.repositories.UserRepository;
import godeck.services.GameCharacterService;
import godeck.utils.ErrorHandler;
import godeck.utils.JSON;

/**
 * Static class to initialize all the pre-defined data in the database.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class DatabaseInicialization {
    // Properties

    private static String environment;
    private static GameCharacterService gameCharacterService;
    private static UserRepository userRepository;

    // Constructors

    /**
     * Main constructor. Should never be called, this is a static class. Uses
     * Autowire to inject the game character service and the user repository.
     * 
     * @param gameCharacterService The game character service.
     * @param userRepository       The user repository.
     * @param environment          The current environment.
     */
    @Autowired
    public DatabaseInicialization(GameCharacterService gameCharacterService, UserRepository userRepository,
            @Value("${environment}") String environment) {
        DatabaseInicialization.gameCharacterService = gameCharacterService;
        DatabaseInicialization.userRepository = userRepository;
        DatabaseInicialization.environment = environment;
    }

    // Private Methods

    /**
     * Gets the file path of the game characters data file according to the
     * current environment.
     * 
     * @return The file path of the game characters data file.
     * @throws RuntimeException
     */
    private static String getGameCharactersDataFilePath() throws RuntimeException {
        if (environment.equals("production")) {
            return "";
        } else if (environment.equals("docker")) {
            return "../data/GameCharactersData.json";
        } else if (environment.equals("dev")) {
            return "src/data/GameCharactersData.json";
        } else {
            throw new RuntimeException(
                    "\u001B[31mERROR!\u001B[0m Environment not recognized. Please check the environment variable. Environment: "
                            + environment);
        }
    }

    /**
     * Reads the game characters data from a JSON file and returns a list of game
     * characters.
     * 
     * @param fileName The file name.
     * @return A list of game characters.
     * @throws Exception If the file is not found or if there is an error reading
     *                   the file.
     */
    @SuppressWarnings("unchecked")
    private static List<GameCharacter> readGameCharactersDataFromFile(String fileName) throws Exception {
        String data = Files.readString(Path.of(fileName));
        List<GameCharacter> gameCharacters = (List<GameCharacter>) JSON.construct(data, GameCharacter.class, true);
        return gameCharacters;
    }

    /**
     * Saves a list of game characters to the database. Also updates the game
     * characters that already exist in the database.
     * 
     * @param gameCharacters The list of game characters.
     */
    private static void saveGameCharactersToDatabase(List<GameCharacter> gameCharacters) {
        for (GameCharacter gameCharacter : gameCharacters) {
            gameCharacterService.save(gameCharacter);
        }
    }

    /**
     * Removes any unknown game characters from the database.
     * 
     * @param gameCharacters The list of game characters.
     */
    private static void removeUnknownGameCharacters(List<GameCharacter> gameCharacters) {
        Iterable<GameCharacter> databaseGameCharacters = gameCharacterService.findAll();
        for (GameCharacter databaseGameCharacter : databaseGameCharacters) {
            if (!gameCharacters.contains(databaseGameCharacter)) {
                gameCharacterService.delete(databaseGameCharacter.getId());
            }
        }
    }

    // Public Methods

    /**
     * Populates the database with the game characters. Reads the data from a JSON
     * file and saves it to the database. Also removes any unknown game characters
     * from the database.
     */
    public static void initializeGameCharacters() {
        try {
            String gameCharactersDataFilePath = getGameCharactersDataFilePath();
            List<GameCharacter> gameCharacters = readGameCharactersDataFromFile(gameCharactersDataFilePath);
            saveGameCharactersToDatabase(gameCharacters);
            removeUnknownGameCharacters(gameCharacters);
        } catch (Exception e) {
            ErrorHandler.message(e);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TODO: This is just a test. Remove it later when oAuth is implemented
    public static void test_initializeUser() {
        Random random = new Random();
        Iterable<GameCharacter> chars = gameCharacterService.findAll();
        List<String> emails = new ArrayList<String>();
        emails.add("arnaldo@email.com");
        emails.add("berenice@email.com");
        emails.add("catarina@email.com");
        emails.add("dorival@email.com");
        emails.add("euclides@email.com");
        emails.add("fagner@email.com");
        emails.add("gerson@email.com");
        for (String email : emails) {
            User user = userRepository.findByEmail(email)
                    .orElse(null);
            if (user != null) {
                continue;
            }
            UUID id = UUID.randomUUID();
            Integer gold = random.nextInt(10000);
            Integer crystals = random.nextInt(100);
            Set<GameCharacter> collection = new HashSet<GameCharacter>();
            ArrayList<GameCharacter> deck = new ArrayList<GameCharacter>(7);
            for (GameCharacter character : chars) {
                collection.add(character);
            }
            deck.addAll(pickRandomGameCharacters(collection, 7));
            user = new User(id, email.substring(0, email.indexOf("@")), email.substring(0, email.indexOf("@")),
                    email, gold, crystals, deck, collection, false, new HashSet<>());
            userRepository.save(user);
        }
    }

    private static List<GameCharacter> pickRandomGameCharacters(Set<GameCharacter> list, int n) {
        List<GameCharacter> randomList = new ArrayList<GameCharacter>();
        List<GameCharacter> characters = new ArrayList<GameCharacter>(list);
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            GameCharacter randomElement = characters.get(r.nextInt(characters.size()));
            randomList.add(randomElement);
            characters.remove(randomElement);
        }
        return randomList;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
