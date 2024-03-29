package godeck.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.entities.GameCharacter;
import godeck.repositories.GameCharacterRepository;

/**
 * Service for the game character model. It is responsible for managing the game
 * character data in the database. It uses the GameCharacterRepository to
 * provide
 * the basic CRUD operations.
 * 
 * @author Bruno Pena Baeta
 */
@Service
public class GameCharacterService {
    // Properties

    private GameCharacterRepository gameCharacterRepository;

    // Constructors

    /**
     * Main constructor. Uses Autowire to inject the GameCharacterRepository.
     * 
     * @param gameCharacterRepository The repository for the game character model.
     */
    @Autowired
    public GameCharacterService(GameCharacterRepository gameCharacterRepository) {
        this.gameCharacterRepository = gameCharacterRepository;
    }

    // Methods

    /**
     * Returns all the game characters from the database.
     * 
     * @return All the game characters from the database.
     */
    public Iterable<GameCharacter> findAll() {
        return gameCharacterRepository.findAll();
    }

    /**
     * Returns the game character from the database by its id. If the game character
     * does not exist, it throws an exception.
     * 
     * @param id The id of the game character.
     * @return The game character from the database.
     */
    public GameCharacter getById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return gameCharacterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Character not found"));
    }

    /**
     * Saves the game character in the database.
     * 
     * @param character The game character to be saved.
     * @return The game character saved in the database.
     */
    public GameCharacter save(GameCharacter character) {
        if (character == null) {
            throw new IllegalArgumentException("Character cannot be null");
        }
        return gameCharacterRepository.save(character);
    }

    /**
     * Updates the game character in the database.
     * 
     * @param character The game character to be updated.
     * @return The game character updated in the database.
     */
    public GameCharacter update(GameCharacter character) {
        if (character == null) {
            throw new IllegalArgumentException("Character cannot be null");
        }
        return gameCharacterRepository.save(character);
    }

    /**
     * Deletes the game character from the database by its id.
     * 
     * @param id The id of the game character.
     */
    public void delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        gameCharacterRepository.deleteById(id);
    }

    /**
     * Deletes all the game characters from the database. This method is used for
     * testing purposes.
     */
    public void deleteAll() {
        gameCharacterRepository.deleteAll();
    }
}
