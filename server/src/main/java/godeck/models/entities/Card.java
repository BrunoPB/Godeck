package godeck.models.entities;

import java.util.UUID;

import godeck.models.client.ClientCard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a game card.
 * 
 * @author Bruno Pena Baeta
 */
@Entity(name = "card")
@Table(name = "card")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Card {
    // Properties

    @Id
    private UUID id;
    @Column(name = "number", nullable = false, unique = false)
    private int number;
    @Column(name = "name", nullable = false, length = 20, unique = false)
    private String name;
    @Column(name = "tier", nullable = false)
    private int tier;
    @Column(name = "mythology", nullable = false)
    private int mythology;
    @Column(name = "fileName", nullable = false, length = 30, unique = false)
    private String fileName;
    @Column(name = "price", nullable = false)
    private int price;
    @Column(name = "stars", nullable = false)
    private int stars;
    @Column(name = "north", nullable = false)
    private int north;
    @Column(name = "northEast", nullable = false)
    private int northEast;
    @Column(name = "southEast", nullable = false)
    private int southEast;
    @Column(name = "south", nullable = false)
    private int south;
    @Column(name = "southWest", nullable = false)
    private int southWest;
    @Column(name = "northWest", nullable = false)
    private int northWest;

    // Constructors

    /**
     * Constructor that receives a ClientCard object and creates a Card object.
     * 
     * @param cCard The ClientCard object.
     */
    public Card(ClientCard cCard) {
        this.id = cCard.getId();
        this.number = cCard.getNumber();
        this.name = cCard.getCardName();
        this.tier = cCard.getTier();
        this.mythology = cCard.getMythology();
        this.fileName = cCard.getFileName();
        this.price = cCard.getPrice();
        this.stars = cCard.getStars();
        this.north = cCard.getNorth();
        this.northEast = cCard.getNorthEast();
        this.southEast = cCard.getSouthEast();
        this.south = cCard.getSouth();
        this.southWest = cCard.getSouthWest();
        this.northWest = cCard.getNorthWest();
    }

    // Methods

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card card = (Card) obj;
            return card.getId().equals(this.id);
        }
        return false;
    }
}
