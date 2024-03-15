package godeck.models;

import org.json.JSONObject;
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
    private boolean exists;

    // Constructors

    /**
     * Default constructor. Used to create a card that does not exist.
     */
    public InGameCard() {
        exists = false;
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
        this.exists = true;
    }

    public InGameCard(String jsonString) {
        JSONObject card = new JSONObject(jsonString);
        this.cardOwner = card.getInt("cardOwner");
        this.currentDominator = card.getInt("currentDominator");
        this.card = new GameCharacter(card.getJSONObject("card").toString());
        this.exists = card.getBoolean("exists");
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

    public boolean exists() {
        return this.exists;
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

    public void setExistance(boolean exists) {
        this.exists = exists;
    }

    // Public Methods

    /**
     * Returns the JSON representation of the card in the game.
     * 
     * @return The JSON representation of the card in the game.
     */
    public String toJSONString() {
        if (exists) {
            return "{\"cardOwner\":" + this.cardOwner + ",\"currentDominator\":" + this.currentDominator + ",\"card\":"
                    + this.card.toJSONString() + ",\"exists\":" + this.exists + "}";
        }
        return "{\"cardOwner\":" + null + ",\"currentDominator\":" + null + ",\"card\":"
                + null + ",\"exists\":" + this.exists + "}";
    }

}
