package com.example.tictactoe.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Conectado a " + host + ":" + port);
            System.out.print("Ingrese su nombre: ");
            String nombre = sc.nextLine().trim();
            out.println("AUTH " + nombre);
            String response = in.readLine();
            if (!"AUTH_OK".equals(response)) {
                System.out.println("Autenticación fallida: " + response);
                return;
            }
            String start = in.readLine(); // START <X|O>
            if (start != null && start.startsWith("START ")) {
                String simbolo = start.substring(6);
                System.out.println("Comienza la partida. Su simbolo: " + simbolo);
            }

            // loop receiving server messages and reacting
            boolean running = true;
            while (running) {
                String serverLine = in.readLine();
                if (serverLine == null) break;
                if (serverLine.startsWith("BOARD")) {
                    // next lines contain board; server sends single string after newline
                    String board = serverLine.substring(5).trim();
                    if (board.isEmpty()) {
                        // sometimes the server uses a separate line after BOARD
                        board = in.readLine();
                    }
                    System.out.println("\nTablero:\n" + board);
                } else if (serverLine.equals("YOUR_TURN")) {
                    System.out.println("Es su turno. Ingrese fila y columna (0-2) separadas por espacio: ");
                    String mv = sc.nextLine().trim();
                    String[] parts = mv.split(" ");
                    if (parts.length != 2) {
                        System.out.println("Formato inválido, intente de nuevo.");
                        continue;
                    }
                    out.println("MOVE " + parts[0] + " " + parts[1]);
                } else if (serverLine.equals("WAIT_TURN")) {
                    System.out.println("Esperando el movimiento del oponente...");
                } else if (serverLine.startsWith("INVALID")) {
                    System.out.println("Movimiento inválido: " + serverLine.substring(8));
                } else if (serverLine.startsWith("RESULT ")) {
                    String res = serverLine.substring(7);
                    switch (res) {
                        case "WIN": System.out.println("¡Ganaste!"); break;
                        case "LOSE": System.out.println("Perdiste."); break;
                        case "DRAW": System.out.println("Empate."); break;
                    }
                    running = false;
                } else {
                    System.out.println("Servidor: " + serverLine);
                }
            }

            System.out.println("Partida finalizada. Gracias por jugar.");
        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
