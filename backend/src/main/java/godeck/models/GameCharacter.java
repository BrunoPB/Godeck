package godeck.models;

import java.util.UUID;

import org.json.JSONObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    // Constructors

    public GameCharacter(String jsonString) {
        JSONObject character = new JSONObject(jsonString);
        this.id = UUID.fromString(character.getString("id"));
        this.number = character.getInt("number");
        this.name = character.getString("name");
        this.tier = character.getInt("tier");
        this.mythology = character.getInt("mythology");
        this.fileName = character.getString("fileName");
        this.price = character.getInt("price");
        this.stars = character.getInt("stars");
        this.north = character.getInt("north");
        this.northEast = character.getInt("northEast");
        this.southEast = character.getInt("southEast");
        this.south = character.getInt("south");
        this.southWest = character.getInt("southWest");
        this.northWest = character.getInt("northWest");
    }

    // Methods

    /**
     * Returns a JSON representation of the game character.
     * 
     * @return A JSON representation of the game character.
     */
    public String toJSONString() {
        return "{\"id\" : \"" + id + "\", \"number\" : " + number + ", \"name\" : \"" + name + "\", \"tier\" : " + tier
                + ", \"mythology\" : " + mythology + ", \"fileName\" : \"" + fileName + "\", \"price\" : " + price
                + ", \"stars\" : " + stars + ", \"north\" : " + north + ", \"northEast\" : " + northEast
                + ", \"southEast\" : " + southEast + ", \"south\" : " + south + ", \"southWest\" : " + southWest
                + ", \"northWest\" : " + northWest + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameCharacter) {
            GameCharacter gameCharacter = (GameCharacter) obj;
            return gameCharacter.getId().equals(this.id);
        }
        return false;
    }
}
