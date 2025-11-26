package com.example.tictactoe.server;

public class ServerMain {
    public static void main(String[] args) {
        int port = 5000;
        if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
        }
        Server server = new Server(port);
        server.start();
    }
}
