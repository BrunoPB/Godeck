package godeck.models.responses;

import org.springframework.stereotype.Component;

import godeck.models.client.ClientUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents a response to any type of login request, such as
 * registarions, creation of ghost users or simple logins.
 * 
 * Is a component. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
public class LoginResponse {
    // Properties

    private boolean status;
    private String token;
    private ClientUser user;
    private String message;
}
