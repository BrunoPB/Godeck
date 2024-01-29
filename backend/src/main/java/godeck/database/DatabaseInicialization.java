package godeck.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import godeck.models.GameCharacter;
import godeck.services.GameCharacterService;

@Component
public class DatabaseInicialization {
    private static GameCharacterService gameCharacterService;

    @Autowired
    public DatabaseInicialization(GameCharacterService gameCharacterService) {
        DatabaseInicialization.gameCharacterService = gameCharacterService;
    }

    public static void initializeGameCharacters() {
        System.out.println("Initializing game characters...");
        System.out.println("Reading data from GameCharactersData.json...");
        List<GameCharacter> gameCharacters = null;
        try {
            gameCharacters = readGameCharactersData("database/GameCharactersData.json");
            if (gameCharacters == null) {
                throw new Exception("\u001B[31mERROR!\u001B[0m Could not read data from GameCharactersData.json");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Data read successfully");
        System.out.println("Saving data to database...");
        for (GameCharacter gameCharacter : gameCharacters) {
            System.out.println("Saving " + gameCharacter.getName() + "...");
            gameCharacterService.save(gameCharacter);
        }
        Iterable<GameCharacter> databaseGameCharacters = gameCharacterService.getAll();
        List<UUID> ids = new ArrayList<>();
        gameCharacters.forEach(gameCharacter -> {
            ids.add(gameCharacter.getId());
        });
        databaseGameCharacters.forEach(gameCharacter -> {
            if (!ids.contains(gameCharacter.getId())) {
                System.out.println("\u001B[31mDeleting unknown game character: " +
                        gameCharacter.getName());
                System.out.println(" \u001B[31mId: " + gameCharacter.getId());
                gameCharacterService.delete(gameCharacter.getId());
            }
        });
        System.out.println("Data saved successfully!");
        System.out.println("Game characters initialized successfully!");
    }

    private static List<GameCharacter> readGameCharactersData(String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new Exception("File " + fileName + " not found");
        }
        Scanner scanner = new Scanner(file);
        String data = "";
        while (scanner.hasNextLine()) {
            data += scanner.nextLine();
        }
        List<GameCharacter> gameCharacters = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(data);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject gameCharacterJson = jsonArray.getJSONObject(i);
            GameCharacter gameCharacter = new GameCharacter(
                    UUID.fromString(gameCharacterJson.getString("id")),
                    gameCharacterJson.getInt("number"),
                    gameCharacterJson.getString("name"),
                    gameCharacterJson.getInt("tier"),
                    gameCharacterJson.getInt("mythology"),
                    gameCharacterJson.getString("file_name"),
                    gameCharacterJson.getInt("price"),
                    gameCharacterJson.getInt("stars"),
                    gameCharacterJson.getInt("north"),
                    gameCharacterJson.getInt("north_east"),
                    gameCharacterJson.getInt("south_east"),
                    gameCharacterJson.getInt("south"),
                    gameCharacterJson.getInt("south_west"),
                    gameCharacterJson.getInt("north_west"));
            gameCharacters.add(gameCharacter);
        }
        scanner.close();
        return gameCharacters;
    }
}
