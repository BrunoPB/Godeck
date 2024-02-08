package godeck.repositories;

import java.util.List;
import java.util.UUID;

import godeck.models.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    List<User> findByEmail(String email);
}