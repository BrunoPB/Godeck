package godeck.models.client;

import org.springframework.stereotype.Component;

import godeck.models.ingame.InGameCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a card in a game. This is how the client will see the cards that
 * are in the game.
 * 
 * @see godeck.models.entities.InGameCard
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientInGameCard {
    // Properties

    private int cardOwner;
    private int currentDominator;
    private ClientCard card;
    private boolean exists = false;

    // Constructors

    /**
     * Constructor that receives an InGameCard object and creates a ClientInGameCard
     * object.
     * 
     * @param inGameCard The InGameCard object.
     */
    public ClientInGameCard(InGameCard inGameCard) {
        this.exists = inGameCard.exists();
        if (exists) {
            this.cardOwner = inGameCard.getCardOwner();
            this.currentDominator = inGameCard.getCurrentDominator();
            this.card = new ClientCard(inGameCard.getCard());
        }
    }

    // Public Methods

    public boolean exists() {
        return this.exists;
    }
}
