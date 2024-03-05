package godeck.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import godeck.models.GameCharacter;

/**
 * Repository for the game character model. It is responsible for managing the
 * game character data in the database. It uses the Spring Data JPA to provide
 * the basic CRUD operations.
 * 
 * @author Bruno Pena Baeta
 */
public interface GameCharacterRepository extends CrudRepository<GameCharacter, UUID> {
}
