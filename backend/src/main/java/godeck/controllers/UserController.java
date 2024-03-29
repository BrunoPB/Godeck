package godeck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import godeck.models.entities.User;
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

    @GetMapping(path = "")
    @ResponseBody
    public User test(@RequestBody String email) { // TODO: Remove this end point when authentication is implemented
        return userRepository.findByEmail(email).get(0);
    }
}
