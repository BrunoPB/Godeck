package godeck.models;

import org.springframework.stereotype.Component;

/**
 * Class that represents a card in a game.
 * 
 * Is a component. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class InGameCard {
    // Properties

    private int cardOwner;
    private int currentDominator;
    private GameCharacter card;

    // Constructors

    /**
     * Default constructor.
     */
    public InGameCard() {
    }

    /**
     * Main constructor.
     * 
     * @param cardOwner The owner of the card.
     * @param card      The card.
     */
    public InGameCard(int cardOwner, GameCharacter card) {
        this.cardOwner = cardOwner;
        this.currentDominator = cardOwner;
        this.card = card;
    }

    // Getters and Setters

    public int getCardOwner() {
        return this.cardOwner;
    }

    public int getCurrentDominator() {
        return this.currentDominator;
    }

    public GameCharacter getCard() {
        return this.card;
    }

    public void setCardOwner(int cardOwner) {
        this.cardOwner = cardOwner;
    }

    public void setCurrentDominator(int currentDominator) {
        this.currentDominator = currentDominator;
    }

    public void setCard(GameCharacter card) {
        this.card = card;
    }

    // Public Methods

    /**
     * Returns the JSON representation of the card in the game.
     * 
     * @return The JSON representation of the card in the game.
     */
    public String toJSONString() {
        return "{\"cardOwner\":" + this.cardOwner + ",\"currentDominator\":" + this.currentDominator + ",\"card\":"
                + this.card.toJSONString() + "}";
    }

}
