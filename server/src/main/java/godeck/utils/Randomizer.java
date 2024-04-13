package godeck.utils;

import org.springframework.stereotype.Component;

/**
 * Static class to handle game randomization.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class Randomizer {
    // Public Methods

    /**
     * Generates a random username.
     * 
     * @return A random username.
     */
    public static String generateUsername() {
        String[] adjectives = { "Happy", "Sad", "Angry", "Calm", "Excited", "Bored", "Tired", "Energetic", "Hungry",
                "Full" };
        String[] animals = { "Dog", "Cat", "Bird", "Fish", "Elephant", "Lion", "Tiger", "Bear", "Wolf", "Fox" };
        String username = adjectives[(int) (Math.random() * adjectives.length)] + animals[(int) (Math.random()
                * animals.length)];
        return username;
    }
}
