package godeck.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import godeck.models.GameCharacter;

public interface GameCharacterRepository extends CrudRepository<GameCharacter, UUID> {
}
