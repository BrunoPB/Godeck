package godeck.models.client;

import java.util.UUID;

import org.springframework.stereotype.Component;

import godeck.models.entities.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents a Card card. This is how the client will see
 * the cards.
 * 
 * @see godeck.models.entities.Card
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientCard {
    // Properties

    private UUID id;
    private int number;
    private String cardName;
    private int tier;
    private int mythology;
    private String fileName;
    private int price;
    private int stars;
    private int north;
    private int northEast;
    private int southEast;
    private int south;
    private int southWest;
    private int northWest;

    // Constructors

    /**
     * Constructor that receives a Card object and creates a ClientCard object.
     * 
     * @param card The Card object.
     */
    public ClientCard(Card card) {
        this.id = card.getId();
        this.number = card.getNumber();
        this.cardName = card.getName();
        this.tier = card.getTier();
        this.mythology = card.getMythology();
        this.fileName = card.getFileName();
        this.price = card.getPrice();
        this.stars = card.getStars();
        this.north = card.getNorth();
        this.northEast = card.getNorthEast();
        this.southEast = card.getSouthEast();
        this.south = card.getSouth();
        this.southWest = card.getSouthWest();
        this.northWest = card.getNorthWest();
    }
}
