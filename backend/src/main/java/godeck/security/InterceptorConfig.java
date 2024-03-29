package godeck.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import godeck.services.LoginService;

/**
 * Configuration class for the interceptors. It is responsible for adding the
 * interceptors to the registry. It is also responsible for defining the paths
 * that the interceptors should intercept.
 * 
 * @author Bruno Pena Baeta
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginService loginService;
    @NonNull
    private static String[] loginPaths = { "/login", "/login/**" };
    private static String errorPath = "/error";

    /**
     * Method for adding the interceptors to the registry. It is responsible for
     * adding the interceptors to the registry and defining the paths that the
     * interceptors should intercept.
     * 
     * @param registry The registry to which the interceptors should be added.
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns(loginPaths);
        registry.addInterceptor(new AuthInterceptor(loginService)).excludePathPatterns(loginPaths)
                .excludePathPatterns(errorPath);
        registry.addInterceptor(new ErrorInterceptor()).addPathPatterns(errorPath).excludePathPatterns(loginPaths);
    }
}
