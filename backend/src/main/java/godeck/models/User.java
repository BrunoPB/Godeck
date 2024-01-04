package godeck.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "name", nullable = false, length = 15, unique = true)
    private String name;
    @Column(name = "email", nullable = false, length = 30, unique = true)
    private String email;
    @Column(name = "gold", nullable = false)
    private Integer gold;
    @Column(name = "crystals", nullable = false)
    private Integer crystals;
    @OneToMany(mappedBy = "id")
    private List<GameCharacter> deck;
    @OneToMany(mappedBy = "id")
    private Set<GameCharacter> collection;

    public User() {
    }

    public User(UUID id, String name, String email, Integer gold, Integer crystals, List<GameCharacter> deck,
            Set<GameCharacter> collection) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gold = gold;
        this.crystals = crystals;
        this.deck = deck;
        this.collection = collection;
    }

    public User(String name, String email, Integer gold, Integer crystals, List<GameCharacter> deck,
            Set<GameCharacter> collection) {
        this.name = name;
        this.email = email;
        this.gold = gold;
        this.crystals = crystals;
        this.deck = deck;
        this.collection = collection;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getGold() {
        return gold;
    }

    public Integer getCrystals() {
        return crystals;
    }

    public List<GameCharacter> getDeck() {
        return deck;
    }

    public Set<GameCharacter> getCollection() {
        return collection;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        if (name.length() > 15) {
            throw new IllegalArgumentException("Name must be less than 15 characters");
        }
        this.name = name;
    }

    public void setEmail(String email) {
        if (email.length() > 30) {
            throw new IllegalArgumentException("Email must be less than 30 characters");
        }
        this.email = email;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public void setCrystals(Integer crystals) {
        this.crystals = crystals;
    }

    public void setDeck(ArrayList<GameCharacter> deck) {
        this.deck = deck;
    }

    public void setCollection(HashSet<GameCharacter> collection) {
        this.collection = collection;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email=" + email +
                ", gold=" + gold +
                ", crystals=" + crystals +
                ", deck=" + deck +
                ", collection=" + collection +
                '}';
    }
}
