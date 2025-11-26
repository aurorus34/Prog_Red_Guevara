package com.example.tictactoe.model;

public class Jugador {
    private final String nombre;
    private final String simbolo;

    public Jugador(String nombre, String simbolo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
    }

    public String getNombre() { return nombre; }
    public String getSimbolo() { return simbolo; }
}
