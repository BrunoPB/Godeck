package godeck.models.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a game character. A game character is a card that can be used in a
 * game.
 * 
 * @author Bruno Pena Baeta
 */
@Entity(name = "game_character")
@Table(name = "game_character")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameCharacter {
    // Properties

    @Id
    private UUID id;
    @Column(name = "number", nullable = false, unique = false)
    private int number;
    @Column(name = "name", nullable = false, length = 15, unique = false)
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

    // Methods

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameCharacter) {
            GameCharacter gameCharacter = (GameCharacter) obj;
            return gameCharacter.getId().equals(this.id);
        }
        return false;
    }
}
