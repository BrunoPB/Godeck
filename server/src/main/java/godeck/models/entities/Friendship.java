package godeck.models.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "friendship")
@Table(name = "friendship")
@NoArgsConstructor
@Getter
@Setter
public class Friendship {
    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user0_id")
    private User user0;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user1_id")
    private User user1;

    // Constructors

    /**
     * All arguments constructor.
     * 
     * @param id
     * @param user0
     * @param user1
     */
    public Friendship(UUID id, User user0, User user1) {
        verifyUsers(user0, user1);
        this.id = id;
        this.user0 = user0;
        this.user1 = user1;
    }

    /**
     * Main constructor.
     * 
     * @param user0 The first user.
     * @param user1 The second user.
     */
    public Friendship(User user0, User user1) {
        verifyUsers(user0, user1);
        this.user0 = user0;
        this.user1 = user1;
    }

    // Private methods

    /**
     * Verify if the users are different.
     * 
     * @param user0 First user.
     * @param user1 Second user.
     */
    private void verifyUsers(User user0, User user1) {
        if (user0.equals(user1)) {
            throw new IllegalArgumentException("Can't start a friendship with 2 equal users.");
        }
    }
}
