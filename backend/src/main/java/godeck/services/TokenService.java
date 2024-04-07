package godeck.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.entities.Token;
import godeck.models.entities.User;
import godeck.repositories.TokenRepository;

/**
 * Service for handling token manipulations.
 * 
 * @author Bruno Pena Baeta
 */
@Service
public class TokenService {
    // Properties

    private TokenRepository tokenRepository;

    // Constructors

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    // CRUD Methods

    /**
     * Gets a token by its ID.
     * 
     * @param id The ID of the token.
     * @return The token found or null if not found.
     */
    public Token getById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is null.");
        }
        return tokenRepository.findById(id).orElse(null);
    }

    /**
     * Gets a token by its token value.
     * 
     * @param token The token value.
     * @return The token found or null if not found.
     */
    public Token getByToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is null or empty.");
        }
        return tokenRepository.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Token not found."));
    }

    /**
     * Saves a token in the database.
     * 
     * @param token The token to save.
     * @return The saved token.
     */
    public Token save(Token token) {
        if (token == null) {
            throw new IllegalArgumentException("Token is null.");
        }
        return tokenRepository.save(token);
    }

    // Private Methods

    // Public Methods

    /**
     * Generates a brand new secure token.
     * 
     * @return The generated token.
     */
    public Token generateToken() {
        byte[] randomBytes = new byte[48]; // So the Base64 enconding gets 64 characters
        new SecureRandom().nextBytes(randomBytes);
        String base64 = Base64.getEncoder().encodeToString(randomBytes);
        return new Token(base64);
    }

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
        Token tokenObject = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not found."));
        return tokenObject.getUser();
    }
}
