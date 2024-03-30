package godeck.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.entities.Card;
import godeck.repositories.CardRepository;

/**
 * Service for the card model. It is responsible for managing the game
 * character data in the database. It uses the CardRepository to
 * provide
 * the basic CRUD operations.
 * 
 * @author Bruno Pena Baeta
 */
@Service
public class CardService {
    // Properties

    private CardRepository cardRepository;

    // Constructors

    /**
     * Main constructor. Uses Autowire to inject the CardRepository.
     * 
     * @param cardRepository The repository for the card model.
     */
    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    // Methods

    /**
     * Returns all the cards from the database.
     * 
     * @return All the cards from the database.
     */
    public Iterable<Card> findAll() {
        return cardRepository.findAll();
    }

    /**
     * Returns the card from the database by its id. If the card
     * does not exist, it throws an exception.
     * 
     * @param id The id of the card.
     * @return The card from the database.
     */
    public Card getById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return cardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Character not found"));
    }

    /**
     * Saves the card in the database.
     * 
     * @param character The card to be saved.
     * @return The card saved in the database.
     */
    public Card save(Card character) {
        if (character == null) {
            throw new IllegalArgumentException("Character cannot be null");
        }
        return cardRepository.save(character);
    }

    /**
     * Updates the card in the database.
     * 
     * @param character The card to be updated.
     * @return The card updated in the database.
     */
    public Card update(Card character) {
        if (character == null) {
            throw new IllegalArgumentException("Character cannot be null");
        }
        return cardRepository.save(character);
    }

    /**
     * Deletes the card from the database by its id.
     * 
     * @param id The id of the card.
     */
    public void delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        cardRepository.deleteById(id);
    }

    /**
     * Deletes all the cards from the database. This method is used for
     * testing purposes.
     */
    public void deleteAll() {
        cardRepository.deleteAll();
    }
}
