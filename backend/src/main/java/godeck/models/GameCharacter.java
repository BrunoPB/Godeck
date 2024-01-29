package godeck.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "game_character")
public class GameCharacter {
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

    public GameCharacter() {
    }

    public GameCharacter(UUID id, int number, String name, int tier, int mythology, String fileName, int price,
            int stars, int north, int northEast, int southEast, int south,
            int southWest, int northWest) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.tier = tier;
        this.mythology = mythology;
        this.fileName = fileName;
        this.price = price;
        this.stars = stars;
        this.north = north;
        this.northEast = northEast;
        this.southEast = southEast;
        this.south = south;
        this.southWest = southWest;
        this.northWest = northWest;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int gettier() {
        return tier;
    }

    public int getMythology() {
        return mythology;
    }

    public String getFileName() {
        return fileName;
    }

    public int getPrice() {
        return price;
    }

    public float getstars() {
        return stars;
    }

    public int getNorth() {
        return north;
    }

    public int getNorthEast() {
        return northEast;
    }

    public int getSouthEast() {
        return southEast;
    }

    public int getSouth() {
        return south;
    }

    public int getSouthWest() {
        return southWest;
    }

    public int getNorthWest() {
        return northWest;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void settier(int tier) {
        this.tier = tier;
    }

    public void setMythology(int mythology) {
        this.mythology = mythology;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setstars(int stars) {
        this.stars = stars;
    }

    public void setNorth(int north) {
        this.north = north;
    }

    public void setNorthEast(int northEast) {
        this.northEast = northEast;
    }

    public void setSouthEast(int southEast) {
        this.southEast = southEast;
    }

    public void setSouth(int south) {
        this.south = south;
    }

    public void setSouthWest(int southWest) {
        this.southWest = southWest;
    }

    public void setNorthWest(int northWest) {
        this.northWest = northWest;
    }
}