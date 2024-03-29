package godeck.security;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor for handling errors. It is responsible for handling errors that
 * occur before, during or after the request handling process.
 * 
 * Is an interceptor. Can be accessed from the web.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class ErrorInterceptor implements HandlerInterceptor {

    /**
     * Method for handling errors that occur before the request handling process.
     * 
     * This methods intercepts the request before it is handled by the controller.
     * 
     * @param request  The request to be checked.
     * @param response The response to be checked.
     * @param handler  The handler to be checked.
     * @return False, as the request is not handled by the controller.
     * @throws Exception If the request is not handled by the controller.
     */
    @Override
    public synchronized boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        return false;
    }
}