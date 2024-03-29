package godeck.models.responses;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a response from the queue service. It is used to store the status
 * of the request, the port number and a message.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QueueResponse extends Object {
    // Properties

    private boolean status;
    private int port;
    private String message;
}
