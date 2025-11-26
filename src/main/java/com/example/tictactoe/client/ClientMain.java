package com.example.tictactoe.client;

public class ClientMain {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;
        if (args.length >= 1) host = args[0];
        if (args.length >= 2) {
            try { port = Integer.parseInt(args[1]); } catch (NumberFormatException ignored) {}
        }
        Client client = new Client(host, port);
        client.start();
    }
}
