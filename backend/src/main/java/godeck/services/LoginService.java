package godeck.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.client.ClientUser;
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
    private FriendshipService friendshipService;

    // Constructors

    @Autowired
    public LoginService(UserService userService, TokenService tokenService, FriendshipService friendshipService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.friendshipService = friendshipService;
    }

    // Public Methods

    /**
     * Logs in a Ghost User. Create it's account and return the response with the
     * new Token.
     * 
     * @return The response with the user and it's token.
     */
    public synchronized LoginResponse loginGhostUser() {
        try {
            User user = userService.generateGhostUser();
            userService.save(user);
            String token = tokenService.generateToken();
            Token tokenObject = new Token(token, user);
            tokenService.save(tokenObject);
            ClientUser userClient = new ClientUser(user);
            return new LoginResponse(true, token, userClient, "Ghost User logged in successfully.");
        } catch (Exception e) {
            return new LoginResponse(false, null, new ClientUser(), e.getMessage());
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
            return new LoginResponse(false, null, new ClientUser(), "Token expired.");
        }

        ClientUser userClient = new ClientUser(tokenObject.getUser(),
                friendshipService.getUserFriendList(tokenObject.getUser().getId()));

        LoginResponse response = new LoginResponse(true, token, userClient, "User logged in successfully.");

        return response;
    }
}
