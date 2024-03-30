package godeck.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import godeck.models.entities.Token;

/**
 * Repository for the token model. It is responsible for managing the token data
 * in the database. It uses the Spring Data JPA to provide the basic CRUD
 * operations.
 * 
 * @author Bruno Pena Baeta
 */
@Repository
public interface TokenRepository extends CrudRepository<Token, UUID> {
    Optional<Token> findByToken(String token);
}
