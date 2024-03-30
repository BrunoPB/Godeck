package godeck.models.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user token. Tokens are used to authenticate users. They are
 * generated when a user logs in.
 * 
 * @author Bruno Pena Baeta
 */
@Entity(name = "token")
@Table(name = "token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Token {
    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "token", nullable = false, unique = true)
    private String token;
    @Column(name = "active", nullable = false)
    private boolean active;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors

    /**
     * Main constructor.
     * 
     * @param token The token.
     * @param user  The user that owns the token.
     */
    public Token(String token, User user) {
        this.token = token;
        this.user = user;
        this.active = true;
    }

    // Public Methods

    /**
     * Reactivates the token.
     */
    public void reactivate() {
        this.active = true;
    }

    /**
     * Deactivates the token.
     */
    public void deactivate() {
        this.active = false;
    }
}
