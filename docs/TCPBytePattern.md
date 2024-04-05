# GODECK TCP BYTE TRANSMISSION PATTERN

This document explains the pattern that should be used in Godeck TCP Connections.

For every single TCP message sent, it should ALWAYS be encoded in Base64 and end with `'\n'`.

There are 2 types of pattern for TCP Connections in Godeck: Server to Client and Client to Server.

## Server to Client

The Server can send multiple informations to the Client, and the Client should decode them.

For this to be possible, the Server will send a TCP message starting with the command, then a ":" and then the command parameter.

The Server should use the `DataOutputStream.writeBytes(string)` method to send messages to the Client.

-   Example: Sending a message to update the current turn

```cpp
out[0].writeBytes(Base64.getEncoder().encodeToString("GameTurn:false".getBytes()) + "\n");
```

### Server commands

-   `GameStart` Confirms to the client that the game is starting. Should get "true" as parameter if the game is really starting, "false" otherwise
-   `UserNumber` Update the client user's number. Should get a number as parameter
-   `GameTurn` Notify the client if the current turn is it's turn to move or not. Should get true as parameter if is the player's turn, false if is the opponent's turn
-   `OpponentInfo` Send the visible opponent information to the player. Should get a Opponent json string as parameter
-   `Deck` Send the client's current in-game deck. Should get a json string with an array of InGameCard objects
-   `Board` Send the client's current board state. Should get a json array string the represents the board. The board is a Vector of Vectors of InGameCards
-   `Timer` Send the current time limit for a turn and restarts the timer. Should get a number as parameter
-   `Update` Updates the client state. Should be sent when the client has already received all the needed information and needs to update it's UI. Does not need parameters
-   `GameEnd` Ends the current game. Should get the reason for the game ending ("Win", "Lose", "SurrenderPlayer", "SurrenderOpponent", "AFKPlayer", "AFKOpponent") as parameter
-   `Error` Notify the client that an error has occurred in the server. Should get the error message as parameter

## Client to Server

The pattern used by the Client is the same as the Server, but with other commands.

The Client should always use the `StreamPeerTCP.put_string(string)` method to send messages to the Server.

-   Example: Sending a message to the Server to notify the Client is surrendering

```cpp
tcp_stream.put_string(Marshalls.raw_to_base64("Lose:Surrender".get_utf8_buffer()) + "\n")
```

### Client commands

-   `GameMove` Tries to make a move in the game, the game move might not be made, it is up to the server to decide. Should get a `GameMove` json string as parameter
-   `Lose` Notify the Server that this Client has lost the game. Should get the reason ("Surrender", "Connection") as parameter

## Starting the TCP communication

To initialize a communication, the Client must have first received it's Symmetric Key, IV (initialization vector) and a Key Number.

Then the Client should authenticate itself by sending, in order, a message with the Key Number and then a message with the string "**TheClientIsReady**" (exactly 16 bytes) encrypted by the Symmetric Key and the IV. The cryptography should be AES/CBC/NoPadding.

The Server will check the Key Number to identify the Symmetric Key it sent to that number and use that Symmetric Key to decrypt the "**TheClientIsReady**" message. If the Server could not decrypt it, it will cancel the connection.

The Server first message will be the the `UserNumber` message, this means the connection was established successfully.
