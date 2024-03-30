package godeck.models.client;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents a Card card. This is how the client will see
 * the cards.
 * 
 * Is a component. Can be accessed from anywhere in the application.
 * 
 * @see godeck.models.entities.Card
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Card {
    // Properties

    private UUID id;
    private int number;
    private String characterName;
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
}
