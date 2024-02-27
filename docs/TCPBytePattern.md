# GODECK TCP BYTE TRANSMISSION PATTERN

This document explains the pattern that should be used in Godeck TCP Connections.

For every single TCP message sent, it should ALWAYS end with `'\n'`.

There are 2 types of pattern for TCP Connections in Godeck: Server to Client and Client to Server.

## Server to Client

The Server can send multiple informations to the Client, and the Client should decode them.

For this to be possible, the Server will send a TCP message starting with the command, then a ":" and then the command parameter.

The Server should use the `DataOutputStream.writeBytes(string)` method to send messages to the Client.

-   Example: Sending a message to update the Client user's number to 1

```cpp
out0.writeBytes("UserNumber:1\n");
```

### Server commands

-   `UserNumber` Update the client user's number. Should get a number as parameter
-   `GameMove` Update the game state based on the parameter move. Should get a `GameMove` string as parameter
-   `GameEnd` Ends the current game. Should get the reason for the game ending ("Win", "Lose", "SurrenderPlayer", "SurrenderOpponent", "AFKPlayer", "AFKOpponent") as parameter

## Client to Server

The pattern used by the Client is the same as the Server, but with other commands.

The Client should always use the `StreamPeerTCP.put_string(string)` method to send messages to the Server.

-   Example: Sending a message to the Server to notify the Client is ready

```cpp
tcp_stream.put_string("Ready:true\n")
```

### Client commands

-   `Ready` Notify the Server that the Client is ready to receive messages. Should get "true" or "false" as parameter
-   `GameMove` Tries to make a move in the game, the game move might not be made, it is up to the server to decide. Should get a `GameMove` string as parameter
-   `Lose` Notify the Server that this Client has lost the game. Should get the reason ("Surrender", "Connection") as parameter

###
