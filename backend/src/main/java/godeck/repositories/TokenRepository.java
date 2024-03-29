package godeck.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import godeck.models.entities.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
    Token findByToken(String token);
}
