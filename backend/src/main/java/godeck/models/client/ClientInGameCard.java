package godeck.models.client;

import org.springframework.stereotype.Component;

import godeck.models.entities.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientInGameCard {
    // Properties

    private int cardOwner;
    private int currentDominator;
    private Card card;
    private boolean exists = false;

    // Constructors

    public ClientInGameCard(int cardOwner, Card card) {
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
