package com.example.tictactoe.model;

public class Tablero {
    private final Celda[][] tablero = new Celda[3][3];

    public Tablero() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                tablero[i][j] = new Celda();
    }

    public synchronized boolean colocarSimbolo(int fila, int col, String simbolo) {
        if (fila < 0 || fila > 2 || col < 0 || col > 2) return false;
        if (!tablero[fila][col].esVacia()) return false;
        tablero[fila][col].marcar(simbolo);
        return true;
    }

    public synchronized boolean esGanador(String simbolo) {
        // filas
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0].getEstado().equals(simbolo)
                && tablero[i][1].getEstado().equals(simbolo)
                && tablero[i][2].getEstado().equals(simbolo)) return true;
        }
        // columnas
        for (int j = 0; j < 3; j++) {
            if (tablero[0][j].getEstado().equals(simbolo)
                && tablero[1][j].getEstado().equals(simbolo)
                && tablero[2][j].getEstado().equals(simbolo)) return true;
        }
        // diagonales
        if (tablero[0][0].getEstado().equals(simbolo)
            && tablero[1][1].getEstado().equals(simbolo)
            && tablero[2][2].getEstado().equals(simbolo)) return true;
        if (tablero[0][2].getEstado().equals(simbolo)
            && tablero[1][1].getEstado().equals(simbolo)
            && tablero[2][0].getEstado().equals(simbolo)) return true;

        return false;
    }

    public synchronized boolean esTableroCompleto() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (tablero[i][j].esVacia()) return false;
        return true;
    }

    public synchronized boolean esEmpate() {
        return esTableroCompleto() && !esGanador("X") && !esGanador("O");
    }

    public synchronized String mostrarTablero() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String e = tablero[i][j].getEstado();
                if ("VACIA".equals(e)) sb.append(" ");
                else sb.append(e);
                if (j < 2) sb.append(" | ");
            }
            if (i < 2) sb.append("\n---------\n");
        }
        return sb.toString();
    }

    public synchronized String toCompactString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                String e = tablero[i][j].getEstado();
                sb.append("VACIA".equals(e) ? "-" : e);
            }
        return sb.toString();
    }
}
