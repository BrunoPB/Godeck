package godeck.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import godeck.models.GameCharacter;
import godeck.repositories.GameCharacterRepository;

@Controller
@RequestMapping("/char")
public class GameCharacterController {
    private GameCharacterRepository gameCharacterRepository;

    @Autowired
    public GameCharacterController(GameCharacterRepository gameCharacterRepository) {
        this.gameCharacterRepository = gameCharacterRepository;
    }

    @PostMapping(path = "")
    @ResponseBody
    public GameCharacter posttest() {
        UUID id = UUID.randomUUID();
        String name = "Zeus";
        GameCharacter character = new GameCharacter(id, name);
        return gameCharacterRepository.save(character);
    }
}
