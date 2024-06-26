package godeck.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReaderHeaderAware;

import godeck.enums.Mythology;
import godeck.models.entities.Card;
import godeck.models.entities.User;
import godeck.repositories.UserRepository;
import godeck.services.CardService;
import godeck.utils.ErrorHandler;

/**
 * Static class to initialize all the pre-defined data in the database.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class DatabaseInicialization {
    // Properties

    private static String environment;
    private static CardService cardService;
    private static UserRepository userRepository;

    // Constructors

    /**
     * Main constructor. Should never be called, this is a static class. Uses
     * Autowire to inject the card service and the user repository.
     * 
     * @param cardService    The card service.
     * @param userRepository The user repository.
     * @param environment    The current environment.
     */
    @Autowired
    public DatabaseInicialization(CardService cardService, UserRepository userRepository,
            @Value("${environment}") String environment) {
        DatabaseInicialization.cardService = cardService;
        DatabaseInicialization.userRepository = userRepository;
        DatabaseInicialization.environment = environment;
    }

    // Private Methods

    /**
     * Gets the file path of the cards data file according to the
     * current environment.
     * 
     * @return The file path of the cards data file.
     * @throws RuntimeException
     */
    private static String getCardsDataFilePath() throws RuntimeException {
        final String EXT = ".csv";
        if (environment.equals("production")) {
            return "";
        } else if (environment.equals("docker")) {
            return "../data/CardsData" + EXT;
        } else if (environment.equals("dev")) {
            return "src/data/CardsData" + EXT;
        } else {
            throw new RuntimeException(
                    "\u001B[31mERROR!\u001B[0m Environment not recognized. Please check the environment variable. Environment: "
                            + environment);
        }
    }

    /**
     * Reads the cards data from a CSV file and returns a list of game
     * characters.
     * 
     * @param fileName The file name.
     * @return A list of cards.
     * @throws Exception If the file is not found or if there is an error reading
     *                   the file.
     */
    private static List<Card> readCardsDataFromFile(String fileName) throws Exception {
        try (CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(new BufferedReader(new FileReader(fileName)))) {
            List<Card> cards = new ArrayList<Card>();
            Map<String, String> data;
            while ((data = csvReader.readMap()) != null) {
                Card card = new Card();
                card.setId(UUID.fromString(data.get("id")));
                card.setName(data.get("name"));
                card.setNumber(Integer.parseInt(data.get("number")));
                card.setTier(Integer.parseInt(data.get("tier")));
                card.setMythology(Mythology.TO_INT.get(data.get("mythology")));
                card.setPrice(Integer.parseInt(data.get("price")));
                card.setStars(Integer.parseInt(data.get("stars")));
                card.setNorth(Integer.parseInt(data.get("north")));
                card.setNorthEast(Integer.parseInt(data.get("northEast")));
                card.setSouthEast(Integer.parseInt(data.get("southEast")));
                card.setSouth(Integer.parseInt(data.get("south")));
                card.setSouthWest(Integer.parseInt(data.get("southWest")));
                card.setNorthWest(Integer.parseInt(data.get("northWest")));
                cards.add(card);
            }
            return cards;
        }
    }

    /**
     * Saves a list of cards to the database. Also updates the game
     * characters that already exist in the database.
     * 
     * @param cards The list of cards.
     */
    private static void saveCardsToDatabase(List<Card> cards) {
        for (Card card : cards) {
            cardService.save(card);
        }
    }

    /**
     * Removes any unknown cards from the database.
     * 
     * @param cards The list of cards.
     */
    private static void removeUnknownCards(List<Card> cards) {
        Iterable<Card> databaseCards = cardService.findAll();
        for (Card databaseCard : databaseCards) {
            if (!cards.contains(databaseCard)) {
                cardService.delete(databaseCard.getId());
            }
        }
    }

    // Public Methods

    /**
     * Populates the database with the cards. Reads the data from a JSON
     * file and saves it to the database. Also removes any unknown cards
     * from the database.
     */
    public static void initializeCards() {
        try {
            String cardsDataFilePath = getCardsDataFilePath();
            List<Card> cards = readCardsDataFromFile(cardsDataFilePath);
            saveCardsToDatabase(cards);
            removeUnknownCards(cards);
        } catch (Exception e) {
            ErrorHandler.message(e);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TODO: This is just a test. Remove it later when oAuth is implemented
    public static void test_initializeUser() {
        Random random = new Random();
        Iterable<Card> chars = cardService.findAll();
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
            Integer platinum = random.nextInt(5);
            Set<Card> collection = new HashSet<Card>();
            ArrayList<Card> deck = new ArrayList<Card>(7);
            for (Card character : chars) {
                collection.add(character);
            }
            deck.addAll(pickRandomCards(collection, 7));
            user = new User(id, email.substring(0, email.indexOf("@")), email.substring(0, email.indexOf("@")),
                    email, gold, crystals, platinum, deck, collection, false);
            userRepository.save(user);
        }
    }

    private static List<Card> pickRandomCards(Set<Card> list, int n) {
        List<Card> randomList = new ArrayList<Card>();
        List<Card> characters = new ArrayList<Card>(list);
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            Card randomElement = characters.get(r.nextInt(characters.size()));
            randomList.add(randomElement);
            characters.remove(randomElement);
        }
        return randomList;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
