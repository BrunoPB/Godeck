# GODECK TCP BYTE TRANSMISSION PATTERN

This document explains the pattern that should be used in Godeck TCP Connections.

For every single TCP message sent, it should ALWAYS end with `'\n'`.

There are 2 types of pattern for TCP Connections in Godeck: Server to Client and Client to Server.

## Server to Client

The Server can send multiple informations to the Client, and the Client should decode them.

For this to be possible, the Server will send a TCP message starting with the command, then a ":" and then the command parameter.

The Server should use the `DataOutputStream.writeBytes(string)` method to send messages to the Client.

-   Example: Sending a message to update the Client user's number to 1

```java
out0.writeBytes("UserNumber:1\n");
```

### Commands

-   `UserNumber` Update the client user's number. Should get a number as parameter
-   `GameMove` Update the game state based on the parameter move. Should get a `GameMove` string as parameter
-   `GameEnd` Ends the current game. Should get the winner and the reason for the win (win or wo) as parameter

## Client to Server

As the Server keeps track of what Clients are doing at all moment, there is no need for the Client to specify the message type.

The only type of message the Client can send to the server is a `GameMove`.
