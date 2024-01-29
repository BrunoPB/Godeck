package godeck.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.GameCharacter;
import godeck.repositories.GameCharacterRepository;

@Service
public class GameCharacterService {
    private GameCharacterRepository gameCharacterRepository;

    @Autowired
    public GameCharacterService(GameCharacterRepository gameCharacterRepository) {
        this.gameCharacterRepository = gameCharacterRepository;
    }

    public Iterable<GameCharacter> getAll() {
        return gameCharacterRepository.findAll();
    }

    public GameCharacter getById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return gameCharacterRepository.findById(id).orElse(null);
    }

    public GameCharacter save(GameCharacter character) {
        if (character == null) {
            throw new IllegalArgumentException("Character cannot be null");
        }
        return gameCharacterRepository.save(character);
    }

    public GameCharacter update(GameCharacter character) {
        if (character == null) {
            throw new IllegalArgumentException("Character cannot be null");
        }
        return gameCharacterRepository.save(character);
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        gameCharacterRepository.deleteById(id);
    }

    public void deleteAll() {
        gameCharacterRepository.deleteAll();
    }

}