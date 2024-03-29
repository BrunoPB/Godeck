package godeck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import godeck.repositories.UserRepository;

/**
 * Controller that handles user manipulation. It is responsible for manipulating
 * the user from http requests.
 * 
 * Is a controller. Can be accessed from the web.
 * 
 * @author Bruno Pena Baeta
 */
@Controller
@RequestMapping("/test")
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
