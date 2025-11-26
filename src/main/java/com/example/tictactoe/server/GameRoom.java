package com.example.tictactoe.server;

import com.example.tictactoe.model.Tablero;

public class GameRoom {
    private final Tablero tablero = new Tablero();
    private ClientHandler playerX;
    private ClientHandler playerO;
    private volatile ClientHandler turno; // who's turn it is

    public synchronized void setPlayers(ClientHandler p1, ClientHandler p2) {
        this.playerX = p1;
        this.playerO = p2;
        this.turno = playerX; // X starts
    }

    public synchronized boolean isFull() {
        return playerX != null && playerO != null;
    }

    public synchronized Tablero getTablero() { return tablero; }

    public synchronized boolean intentarMovimiento(ClientHandler jugador, int fila, int col) {
        if (jugador != turno) return false;
        String simbolo = jugador.getSimbolo();
        boolean ok = tablero.colocarSimbolo(fila, col, simbolo);
        if (!ok) return false;
        // after successful move, switch turn
        turno = (turno == playerX) ? playerO : playerX;
        return true;
    }

    public synchronized ClientHandler getTurno() { return turno; }

    public synchronized ClientHandler getOpponent(ClientHandler c) {
        if (c == playerX) return playerO;
        if (c == playerO) return playerX;
        return null;
    }
}
