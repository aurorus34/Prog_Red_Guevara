package com.example.tictactoe.model;

public class Celda {
    private String estado; // "VACIA", "X", "O"

    public Celda() { this.estado = "VACIA"; }

    public synchronized void vaciar() { this.estado = "VACIA"; }

    public synchronized void marcar(String simbolo) { this.estado = simbolo; }

    public synchronized boolean esVacia() { return "VACIA".equals(this.estado); }

    public synchronized String getEstado() { return estado; }
}
