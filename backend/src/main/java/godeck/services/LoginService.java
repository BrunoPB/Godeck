package godeck.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.entities.Token;
import godeck.models.entities.User;
import godeck.models.responses.LoginResponse;

/**
 * Service for handling login manipulations from the controller http requests.
 * 
 * @author Bruno Pena Baeta
 */
@Service
public class LoginService {
    // Properties

    private UserService userService;
    private TokenService tokenService;

    // Constructors

    @Autowired
    public LoginService(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    // Public Methods

    /**
     * Logs in a Ghost User. Create it's account and return the response with the
     * new Token.
     * 
     * @return The response with the user and it's token.
     */
    public LoginResponse loginGhostUser() {
        try {
            User user = userService.generateGhostUser();
            userService.save(user);
            String token = tokenService.generateToken();
            Token tokenObject = new Token(token, user);
            tokenService.save(tokenObject);
            return new LoginResponse(true, token, user, "Ghost User logged in successfully.");
        } catch (Exception e) {
            return new LoginResponse(false, null, new User(), e.getMessage());
        }
    }

    /**
     * Logs in a user with a token and returns the response.
     * 
     * @param token The token to log in the user.
     * @return The response with the user and it's token.
     */
    public synchronized LoginResponse login(String token) {
        Token tokenObject = tokenService.getByToken(token);

        if (!tokenObject.isActive()) {
            return new LoginResponse(false, null, new User(), "Token expired.");
        }

        LoginResponse response = new LoginResponse(true, token, tokenObject.getUser(), "User logged in successfully.");

        return response;
    }
}
