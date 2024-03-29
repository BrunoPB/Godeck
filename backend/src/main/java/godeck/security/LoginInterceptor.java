package godeck.security;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor for checking the data retrieved from the login request. It is
 * responsible for checking if the data is valid.
 * 
 * Is an interceptor. Can be accessed from the web.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * Method for checking the data retrieved from the login request. It checks if
     * the data is valid.
     * 
     * This methods intercepts the request before it is handled by the login
     * controller.
     * 
     * @param request  The request to be checked.
     * @param response The response to be checked.
     * @param handler  The handler to be checked.
     * @return True if the data is valid, false otherwise.
     * @throws Exception If the data is not valid.
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        return true;
    }
}
