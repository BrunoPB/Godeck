package godeck.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import godeck.models.entities.Friendship;
import godeck.models.entities.User;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, UUID> {

        @Query(value = "SELECT u " +
                        "FROM user u " +
                        "JOIN friendship f ON u.id = f.user0.id OR u.id = f.user1.id " +
                        "WHERE f.user0.id = ?1 OR f.user1.id = ?1")
        List<User> getUserFriendList(UUID userId);

        @Query(value = "SELECT f " +
                        "FROM friendship f " +
                        "WHERE f.user0.id = ?1 OR f.user1.id = ?1")
        List<Friendship> getUserFriendshipList(UUID userId);

        @Query(value = "SELECT f " +
                        "FROM friendship f " +
                        "WHERE (f.user0.id = ?1 AND f.user1.id = ?2) OR (f.user0.id = ?2 AND f.user1.id = ?1)")
        Optional<Friendship> getFriendship(UUID userId1, UUID userId2);
}
