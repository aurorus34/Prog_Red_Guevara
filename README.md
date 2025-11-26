# TicTacToe Network (Server + Client)
Java 17 Maven project (console) with a TCP-based TicTacToe for two players.

## How to build
```bash
mvn package
```

## Run server
```bash
java -cp target/tictactoe-network-1.0-SNAPSHOT.jar com.example.tictactoe.server.ServerMain 5000
```

## Run client (two instances)
```bash
java -cp target/tictactoe-network-1.0-SNAPSHOT.jar com.example.tictactoe.client.ClientMain localhost 5000
```

## Protocol (simple text)
- Client -> Server: `AUTH <name>`
- Server -> Client: `AUTH_OK` or error
- Server -> Client: `START X` or `START O`
- Client -> Server: `MOVE <row> <col>` (row and col 0..2)
- Server -> Client: `BOARD\n<board-as-text>`
- Server -> Client: `YOUR_TURN`, `WAIT_TURN`
- Server -> Client: `INVALID <reason>`
- Server -> Client: `RESULT WIN|LOSE|DRAW`
