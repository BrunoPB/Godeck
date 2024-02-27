package godeck.components.database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import godeck.models.GameCharacter;
import godeck.models.User;
import godeck.repositories.UserRepository;
import godeck.services.GameCharacterService;

@Component
public class DatabaseInicialization {
    private static String environment;
    private static GameCharacterService gameCharacterService;
    private static UserRepository userRepository;

    @Autowired
    public DatabaseInicialization(GameCharacterService gameCharacterService, UserRepository userRepository,
            @Value("${environment}") String environment) {
        DatabaseInicialization.gameCharacterService = gameCharacterService;
        DatabaseInicialization.userRepository = userRepository;
        DatabaseInicialization.environment = environment;
    }

    // TODO: This is just a test. Remove it later when oAuth is implemented
    @SuppressWarnings("null")
    public static void test_initializeUser() {
        System.out.println("Initializing test users...");
        Random random = new Random();
        Iterable<GameCharacter> chars = gameCharacterService.findAll();
        List<String> emails = new ArrayList<String>();
        emails.add("arnaldo@email.com");
        emails.add("berenice@email.com");
        emails.add("catarina@email.com");
        emails.add("dorival@email.com");
        for (String email : emails) {
            List<User> users = userRepository.findByEmail(email);
            if (users.size() > 0) {
                continue;
            }
            UUID id = UUID.randomUUID();
            Integer gold = random.nextInt(10000);
            Integer crystals = random.nextInt(100);
            Set<GameCharacter> collection = new HashSet<GameCharacter>();
            List<GameCharacter> deck = new ArrayList<GameCharacter>();
            for (GameCharacter character : chars) {
                collection.add(character);
            }
            deck.addAll(pickRandomGameCharacters(collection, 7));
            User user = new User(id, email.substring(0, email.indexOf("@")), email, gold, crystals, deck,
                    collection);
            System.out.println("Saving user " + user.getName() + " to database...");
            userRepository.save(user);
        }
        System.out.println("All users initialized successfully!");
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

    public static void initializeGameCharacters() {
        List<GameCharacter> gameCharacters = null;
        try {
            System.out.println("Environment: " + environment);
            System.out.println("Initializing game characters...");
            System.out.println("Reading data from GameCharactersData.json...");
            String gameCharactersDataSource = "";
            if (environment.equals("production")) {
                gameCharactersDataSource = "";
            } else if (environment.equals("docker")) {
                gameCharactersDataSource = "../data/GameCharactersData.json";
            } else if (environment.equals("dev")) {
                gameCharactersDataSource = "src/data/GameCharactersData.json";
            } else {
                throw new Exception(
                        "\u001B[31mERROR!\u001B[0m Environment not recognized. Please check the environment variable. Environment: "
                                + environment);
            }
            gameCharacters = readGameCharactersData(gameCharactersDataSource);
            if (gameCharacters == null) {
                throw new Exception("\u001B[31mERROR!\u001B[0m Could not read data from GameCharactersData.json");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Data read successfully");
        System.out.println("Saving data to database...");
        for (GameCharacter gameCharacter : gameCharacters) {
            System.out.println("Saving " + gameCharacter.getName() + "...");
            gameCharacterService.save(gameCharacter);
        }
        Iterable<GameCharacter> databaseGameCharacters = gameCharacterService.findAll();
        List<UUID> ids = new ArrayList<>();
        gameCharacters.forEach(gameCharacter -> {
            ids.add(gameCharacter.getId());
        });
        databaseGameCharacters.forEach(gameCharacter -> {
            if (!ids.contains(gameCharacter.getId())) {
                System.out.println("\u001B[31mDeleting unknown game character: " +
                        gameCharacter.getName());
                System.out.println(" \u001B[31mId: " + gameCharacter.getId());
                gameCharacterService.delete(gameCharacter.getId());
            }
        });
        System.out.println("Data saved successfully!");
        System.out.println("Game characters initialized successfully!");
    }

    private static List<GameCharacter> readGameCharactersData(String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new Exception("File " + fileName + " not found");
        }
        Scanner scanner = new Scanner(file);
        String data = "";
        while (scanner.hasNextLine()) {
            data += scanner.nextLine();
        }
        List<GameCharacter> gameCharacters = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(data);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject gameCharacterJson = jsonArray.getJSONObject(i);
            GameCharacter gameCharacter = new GameCharacter(
                    UUID.fromString(gameCharacterJson.getString("id")),
                    gameCharacterJson.getInt("number"),
                    gameCharacterJson.getString("name"),
                    gameCharacterJson.getInt("tier"),
                    gameCharacterJson.getInt("mythology"),
                    gameCharacterJson.getString("file_name"),
                    gameCharacterJson.getInt("price"),
                    gameCharacterJson.getInt("stars"),
                    gameCharacterJson.getInt("north"),
                    gameCharacterJson.getInt("north_east"),
                    gameCharacterJson.getInt("south_east"),
                    gameCharacterJson.getInt("south"),
                    gameCharacterJson.getInt("south_west"),
                    gameCharacterJson.getInt("north_west"));
            gameCharacters.add(gameCharacter);
        }
        scanner.close();
        return gameCharacters;
    }
}