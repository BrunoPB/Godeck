package godeck.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.entities.GameCharacter;
import godeck.models.entities.Token;
import godeck.models.entities.User;
import godeck.models.responses.LoginResponse;
import godeck.repositories.TokenRepository;
import godeck.repositories.UserRepository;

/**
 * Service for handling login manipulations from the controller http requests.
 * 
 * @author Bruno Pena Baeta
 */
@Service
public class LoginService {
    // Properties

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private GameCharacterService gameCharacterService;

    // Constructors

    @Autowired
    public LoginService(UserRepository userRepository, TokenRepository tokenRepository,
            GameCharacterService gameCharacterService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.gameCharacterService = gameCharacterService;
    }

    // Private Methods

    /**
     * Generates a token for a user.
     * 
     * @return The generated token.
     */
    private String generateToken() {
        String value = UUID.randomUUID().toString() + Date.valueOf(java.time.LocalDate.now()).toString();
        String encodedToken = Base64.getEncoder().encodeToString(value.getBytes());
        return encodedToken;
    }

    /**
     * Generates a list of cards for ghost users.
     * 
     * @return The list of cards for ghost users.
     */
    private List<GameCharacter> generateGhostUserCards() { // TODO: Establish the initial cards for ghost users
        List<GameCharacter> fullChars = (List<GameCharacter>) gameCharacterService.findAll();
        List<GameCharacter> randomList = new ArrayList<GameCharacter>();
        List<GameCharacter> characters = new ArrayList<GameCharacter>(fullChars);
        Random r = new Random();
        for (int i = 0; i < 7; i++) {
            GameCharacter randomElement = characters.get(r.nextInt(characters.size()));
            randomList.add(randomElement);
            characters.remove(randomElement);
        }
        return randomList;
    }

    // Public Methods

    /**
     * Checks if a token is valid and returns the user that owns it. If the token is
     * invalid, an exception is thrown.
     * 
     * @param token The token to check.
     * @return The user that owns the token.
     * @throws IllegalArgumentException If the token is null or empty or if the
     *                                  token is not found.
     */
    public User checkToken(String token) throws IllegalArgumentException {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is null or empty.");
        }

        Token tokenObject = tokenRepository.findByToken(token);
        if (tokenObject == null) {
            throw new IllegalArgumentException("Token not found.");
        }
        return tokenObject.getUser();
    }

    /**
     * Creates a ghost user, generates it's token and returns the response.
     * 
     * @return The response with the ghost user and it's token.
     */
    public synchronized LoginResponse createGhostUser() {
        // Generate user
        int numberOfUsers = ((Collection<?>) userRepository.findAll()).size();
        User user = new User();
        user.makeGhostUser("user0" + numberOfUsers);
        List<GameCharacter> cards = generateGhostUserCards();
        user.setCollection(new HashSet<GameCharacter>(cards));
        user.setDeck(cards);

        // Save user
        user = userRepository.save(user);

        // Generate token
        String encodedToken = generateToken();
        Token token = new Token(encodedToken, user);

        // Save token
        token = tokenRepository.save(token);

        // Generate response
        LoginResponse response = new LoginResponse(true, encodedToken, user, "Ghost user created successfully.");

        return response;
    }

    /**
     * Logs in a user with a token and returns the response.
     * 
     * @param token The token to log in the user.
     * @return The response with the user and it's token.
     */
    public synchronized LoginResponse login(String token) {
        Token tokenObject = tokenRepository.findByToken(token);
        if (tokenObject == null) {
            return new LoginResponse(false, null, new User(), "Token not found.");
        } else if (!tokenObject.isActive()) {
            return new LoginResponse(false, null, new User(), "Token expired.");
        }

        LoginResponse response = new LoginResponse(true, token, tokenObject.getUser(), "User logged in successfully.");

        return response;
    }
}
