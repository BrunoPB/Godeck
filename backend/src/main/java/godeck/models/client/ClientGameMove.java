package godeck.models.client;

import org.springframework.stereotype.Component;

import godeck.models.Coordinates;
import godeck.models.in_game.InGameCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientGameMove {
    // Properties

    private int player;
    private int deckIndex;
    private Coordinates coords;
    private InGameCard in_game_card;
}
