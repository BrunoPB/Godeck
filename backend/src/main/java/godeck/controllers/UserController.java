package godeck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import godeck.models.User;
import godeck.repositories.UserRepository;

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
    public Iterable<User> test() {
        return userRepository.findAll();
    }
}
