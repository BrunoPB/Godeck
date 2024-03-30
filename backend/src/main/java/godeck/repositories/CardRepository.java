package godeck.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import godeck.models.entities.Card;

/**
 * Repository for the card model. It is responsible for managing the
 * card data in the database. It uses the Spring Data JPA to provide
 * the basic CRUD operations.
 * 
 * @author Bruno Pena Baeta
 */
public interface CardRepository extends CrudRepository<Card, UUID> {
}
