package godeck.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import godeck.models.GameCharacter;
import godeck.models.User;
import godeck.repositories.GameCharacterRepository;
import godeck.repositories.UserRepository;

@Controller
@RequestMapping("/test")
public class UserController {
    private UserRepository userRepository;
    private GameCharacterRepository gameCharacterRepository;

    @Autowired
    public UserController(UserRepository userRepository, GameCharacterRepository gameCharacterRepository) {
        this.userRepository = userRepository;
        this.gameCharacterRepository = gameCharacterRepository;
    }

    @GetMapping(path = "")
    @ResponseBody
    public Iterable<User> test() {
        return userRepository.findAll();
    }

    @PostMapping(path = "")
    @ResponseBody
    public User posttest() {
        UUID id = UUID.randomUUID();
        Integer gold = 100;
        Integer crystals = 5;
        Iterable<GameCharacter> chars = getchars();
        Set<GameCharacter> collection = new HashSet<GameCharacter>();
        System.out.println(collection);
        List<GameCharacter> deck = new ArrayList<GameCharacter>();
        for (GameCharacter character : chars) {
            collection.add(character);
            deck.add(character);
        }
        User user = new User(id, "bruno", "test@email", gold, crystals, deck, collection);
        return userRepository.save(user);
    }

    public Iterable<GameCharacter> getchars() {
        return gameCharacterRepository.findAll();
    }
}
