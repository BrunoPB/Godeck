package godeck.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import godeck.models.responses.LoginResponse;
import godeck.services.LoginService;

/**
 * Controller for handling login, logout and register requests. It is
 * responsible for authenticating users and creating new users.
 * 
 * Is a controller. Can be accessed from the web.
 * 
 * @author Bruno Pena Baeta
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(path = "")
    @ResponseBody
    public LoginResponse login(@RequestBody String token) {
        return loginService.login(token);
    }

    @PostMapping(path = "/ghostuser")
    @ResponseBody
    public LoginResponse loginGhostUser() {
        return loginService.loginGhostUser();
    }
}