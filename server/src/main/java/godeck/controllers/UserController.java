package godeck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import godeck.models.client.Friend;
import godeck.models.entities.User;
import godeck.services.UserService;

/**
 * Controller that handles user manipulation. It is responsible for manipulating
 * the user from http requests.
 * 
 * Is a controller. Can be accessed from the web.
 * 
 * @author Bruno Pena Baeta
 */
@Controller
@RequestMapping("/")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "friend")
    @ResponseBody
    public Friend addFriend(@RequestAttribute User user, @RequestBody String friendUsername) {
        return userService.addFriend(user, friendUsername);
    }
}
