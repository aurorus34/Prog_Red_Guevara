package com.example.tictactoe.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private volatile boolean running = true;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final Queue<ClientHandler> waiting = new ArrayDeque<>();

    public Server(int port) { this.port = port; }

    public void start() {
        System.out.println("Servidor iniciando en puerto " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getRemoteSocketAddress());
                ClientHandler handler = new ClientHandler(clientSocket, this);
                pool.submit(handler);
            }
        } catch (IOException e) {
            System.err.println("Error en servidor: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    // Pair clients into GameRoom
    public synchronized GameRoom joinRoom(ClientHandler handler) {
        try {
            waiting.add(handler);
            if (waiting.size() >= 2) {
                ClientHandler p1 = waiting.poll();
                ClientHandler p2 = waiting.poll();
                GameRoom room = new GameRoom();
                // assign players: p1 -> X, p2 -> O
                room.setPlayers(p1, p2);
                p1.setRoom(room);
                p2.setRoom(room);
                p1.assignSymbol("X");
                p2.assignSymbol("O");
                return room;
            } else {
                // not enough players yet; handler will wait until paired
                while (!waiting.contains(handler)) {
                    // should not happen
                    Thread.sleep(50);
                }
                // if still in queue, return room null; the handler will be paired later by other joiners
                return waiting.size() >= 2 ? null : waiting.peek() != null ? null : null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
