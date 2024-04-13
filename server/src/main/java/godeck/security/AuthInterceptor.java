package godeck.security;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import godeck.models.entities.User;
import godeck.services.TokenService;
import godeck.utils.Printer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor for checking if the user is authenticated. It is responsible for
 * checking if the user has a valid token. If the token is invalid, the user is
 * not authenticated.
 * 
 * Is an interceptor. Can be accessed from the web.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private TokenService tokenService;

    /**
     * Constructor for the AuthInterceptor class.
     * 
     * @param loginService The login service to be used by the interceptor.
     */
    public AuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Method for checking if the user is authenticated. It checks if the user has a
     * valid token. If the token is invalid, the user is not authenticated.
     * 
     * This methods intercepts the request before it is handled by the controller.
     * 
     * @param request  The request to be checked.
     * @param response The response to be checked.
     * @param handler  The handler to be checked.
     * @return True if the user is authenticated, false otherwise.
     * @throws Exception If the user is not authenticated.
     */
    @Override
    public synchronized boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        User tokenUser = new User();
        try {
            tokenUser = tokenService.getByToken(token).getUser();
        } catch (IllegalArgumentException e) {
            String message = "Unauthorized. " + e.getMessage();
            if (token != null && !token.isBlank()) {
                message += " Tried with Token: " + token;
            }
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
            Printer.printWarn(message);
            return false;
        }
        request.setAttribute("user", tokenUser);
        return true;
    }
}
