package godeck.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import godeck.models.entities.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, UUID> {
    Optional<Token> findByToken(String token);
}
