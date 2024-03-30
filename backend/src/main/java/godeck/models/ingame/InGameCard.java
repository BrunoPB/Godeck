package godeck.models.ingame;

import org.springframework.stereotype.Component;

import godeck.models.entities.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents a card in a game.
 * 
 * Is a component. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InGameCard {
    // Properties

    private int cardOwner;
    private int currentDominator;
    private Card card;
    private boolean exists = false;

    // Constructors

    /**
     * Main constructor.
     * 
     * @param cardOwner The owner of the card.
     * @param card      The card.
     */
    public InGameCard(int cardOwner, Card card) {
        this.cardOwner = cardOwner;
        this.currentDominator = cardOwner;
        this.card = card;
        this.exists = true;
    }

    // Public Methods

    public boolean exists() {
        return this.exists;
    }
}
