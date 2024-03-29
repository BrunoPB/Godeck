package godeck.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import godeck.models.entities.User;

/**
 * Repository for the user model. It is responsible for managing the user data
 * in
 * the database. It uses the Spring Data JPA to provide the basic CRUD
 * operations.
 * 
 * @author Bruno Pena Baeta
 */
@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    List<User> findByEmail(String email);
}