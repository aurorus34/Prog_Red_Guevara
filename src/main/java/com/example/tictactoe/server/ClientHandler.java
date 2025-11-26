package com.example.tictactoe.server;

import com.example.tictactoe.model.Tablero;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private BufferedReader in;
    private PrintWriter out;
    private String simbolo;
    private String nombre;
    private GameRoom room;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public String getSimbolo() { return simbolo; }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // Simple authentication: client sends: AUTH <name>
            String line = in.readLine();
            if (line == null || !line.startsWith("AUTH ")) {
                closeSilently();
                return;
            }
            nombre = line.substring(5).trim();
            out.println("AUTH_OK");
            // join a room (server pairs two clients)
            room = server.joinRoom(this);
            if (room == null) {
                out.println("ERROR No room available");
                closeSilently();
                return;
            }

            // assign symbol based on which slot this handler is in
            if (room.getTurno() == this) simbolo = "X"; else simbolo = "O";
            // Inform player
            out.println("START " + simbolo);
            // Wait until both players ready
            while (!room.isFull()) {
                Thread.sleep(100);
            }
            // Notify whose turn
            sendMessage("BOARD\n" + room.getTablero().mostrarTablero());
            if (room.getTurno() == this) sendMessage("YOUR_TURN"); else sendMessage("WAIT_TURN");

            // game loop
            while (true) {
                String request = in.readLine();
                if (request == null) break;
                if (request.startsWith("MOVE ")) {
                    String[] parts = request.substring(5).trim().split(" ");
                    if (parts.length != 2) {
                        sendMessage("INVALID Invalid MOVE format");
                        continue;
                    }
                    int fila = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    synchronized (room) {
                        if (room.getTurno() != this) {
                            sendMessage("INVALID Not your turn");
                            continue;
                        }
                        boolean ok = room.intentarMovimiento(this, fila, col);
                        if (!ok) {
                            sendMessage("INVALID Cell occupied or invalid");
                            continue;
                        }
                        // broadcast updated board to both players
                        ClientHandler opponent = room.getOpponent(this);
                        String boardStr = room.getTablero().mostrarTablero();
                        sendMessage("BOARD\n" + boardStr);
                        if (opponent != null) opponent.sendMessage("BOARD\n" + boardStr);

                        // check win/draw
                        Tablero t = room.getTablero();
                        if (t.esGanador(this.simbolo)) {
                            sendMessage("RESULT WIN");
                            if (opponent != null) opponent.sendMessage("RESULT LOSE");
                            break;
                        } else if (t.esEmpate()) {
                            sendMessage("RESULT DRAW");
                            if (opponent != null) opponent.sendMessage("RESULT DRAW");
                            break;
                        } else {
                            // notify next player
                            ClientHandler next = room.getTurno();
                            if (next != null) next.sendMessage("YOUR_TURN");
                            if (opponent != null && opponent != next) opponent.sendMessage("WAIT_TURN");
                        }
                    }
                } else if (request.equals("QUIT")) {
                    break;
                } else {
                    sendMessage("UNKNOWN_CMD");
                }
            }
        } catch (IOException e) {
            System.err.println("I/O error with client: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            closeSilently();
        }
    }

    public void sendMessage(String msg) {
        if (out != null) out.println(msg);
    }

    public void assignSymbol(String s) { this.simbolo = s; }

    public void setRoom(GameRoom r) { this.room = r; }

    private void closeSilently() {
        try { if (socket != null) socket.close(); } catch (IOException ignored) {}
    }
}
