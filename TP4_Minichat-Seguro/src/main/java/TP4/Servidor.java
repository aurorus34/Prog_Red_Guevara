package TP4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Servidor {

    private static final int PORT = 3500;
    public static final List<ClientHandler> connectedClients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        new Servidor().startServer();
    }

    public void startServer() {
        System.out.println("--- SYSTEM: Server Initializing on port " + PORT + " ---");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Waiting for incoming connections...");
                Socket clientSocket = serverSocket.accept();
                
                System.out.println(Utils.COLORES[3] + "Connection accepted from: " + clientSocket.getInetAddress() + Utils.RESET);

                ClientHandler worker = new ClientHandler(clientSocket);
                Thread thread = new Thread(worker);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Server Critical Error: " + e.getMessage());
        }
    }

    
    public static void removeClient(ClientHandler client) {
        connectedClients.remove(client);
        broadcastSystemMessage(client.getUsername(), false);
    }

    public static void addClient(ClientHandler client) {
        connectedClients.add(client);
        broadcastSystemMessage(client.getUsername(), true);
    }

    private static void broadcastSystemMessage(String username, boolean joined) {
        String msg = joined ? " se ha unido al chat." : " se ha desconectado.";
        String fullMsg = Utils.COLORES[5] + "\t--- " + username + msg + " ---" + Utils.RESET;
        
        synchronized (connectedClients) {
            for (ClientHandler c : connectedClients) {
                if (c.isActive() && !c.getUsername().equals(username)) {
                    c.sendRawMessage(fullMsg);
                }
            }
        }
    }
}


class ClientHandler implements Runnable {

    private String username = "Anonymous";
    private final Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean active = false;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            
            System.out.println("Identificando usuario...");
            String reqId = in.readUTF();
            this.username = (reqId == null || reqId.isBlank()) ? "user_" + socket.getPort() : reqId;
            
            this.active = true;
            Servidor.addClient(this);
            
            System.out.println(Utils.COLORES[1] + "Usuario registrado: " + this.username + Utils.RESET);
            
        } catch (IOException e) {
            System.err.println("Error durante el handshake: " + e.getMessage());
            closeConnection();
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void run() {
        try {
            while (active && socket.isConnected()) {
                String header = in.readUTF();
                
                switch (header) {
                    case "FORWARD::PRIVATE":
                        processPrivateForwarding();
                        break;
                    case "FORWARD::BROADCAST":
                        processBroadcastForwarding();
                        break;
                    case "/listar":
                        sendUserList();
                        break;
                    case "/salir":
                        System.out.println(Utils.COLORES[4] + "Solicitud de desconexión recibida de " + username + Utils.RESET);
                        closeConnection();
                        break;
                    default:
                        handleLegacyMessages(header);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Conexión perdida con " + username);
        } finally {
            closeConnection();
        }
    }


    private void processPrivateForwarding() throws IOException {
        // Lectura de metadatos del paquete
        String targetUser = in.readUTF();
        byte[] iv = readByteArray();
        byte[] cipherData = readByteArray();
        String type = in.readUTF();
        String extra = in.readUTF();

        boolean delivered = false;

        synchronized (Servidor.connectedClients) { 
            for (ClientHandler client : Servidor.connectedClients) { 
                if (client.getUsername().equalsIgnoreCase(targetUser) && client.isActive()) {
                    client.sendEncryptedPacket("INCOMING::PRIVATE", this.username, iv, cipherData, type, extra);
                    delivered = true;
                    break;
                }
            }
        }

        if (!delivered) {
            sendError("RESP::NOTFOUND", targetUser);
        }
    }

    private void processBroadcastForwarding() throws IOException {
        byte[] iv = readByteArray();
        byte[] cipherData = readByteArray();
        String type = in.readUTF();
        String extra = in.readUTF();

        synchronized (Servidor.connectedClients) { 
            for (ClientHandler client : Servidor.connectedClients) {
                if (!client.getUsername().equals(this.username) && client.isActive()) {
                    client.sendEncryptedPacket("INCOMING::BROADCAST", this.username, iv, cipherData, type, extra);
                }
            }
        }
    }


    private void sendUserList() {
        String userList;
        synchronized (Servidor.connectedClients) { 
            userList = Servidor.connectedClients.stream()
                    .map(ClientHandler::getUsername)
                    .collect(Collectors.joining(", "));
        }
        try {
            synchronized (out) {
                out.writeUTF("RESP::LIST");
                out.writeUTF(userList);
                out.flush();
            }
        } catch (IOException e) {
        }
    }

    private void handleLegacyMessages(String message) {
        String target = "Todos";
        String content = message;

        if (message.contains("&")) {
            String[] parts = message.split("&", 2);
            target = parts[0].trim();
            content = (parts.length > 1) ? parts[1].trim() : "";
        }

        System.out.println(Utils.COLORES[1] + "Legacy MSG from " + username + " to " + target + ": " + content + Utils.RESET);

        if (content.isEmpty()) return;

        String finalMsg = this.username + ": " + content;
        
        synchronized (Servidor.connectedClients) { 
            for (ClientHandler c : Servidor.connectedClients) {
                if (!c.isActive()) continue;

                boolean isBroadcast = target.equalsIgnoreCase("Todos") && !c.getUsername().equals(this.username);
                boolean isDirect = target.equalsIgnoreCase(c.getUsername());

                if (isBroadcast || isDirect) {
                    c.sendRawMessage(finalMsg);
                }
            }
        }
    }


    public void sendEncryptedPacket(String header, String sender, byte[] iv, byte[] data, String type, String extra) {
        try {
            synchronized (out) {
                out.writeUTF(header);
                out.writeUTF(sender);
                out.writeInt(iv.length);
                out.write(iv);
                out.writeInt(data.length);
                out.write(data);
                out.writeUTF(type);
                out.writeUTF(extra != null ? extra : "");
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Fallo enviando paquete a " + username);
        }
    }

    public void sendRawMessage(String msg) {
        try {
            synchronized (out) {
                out.writeUTF(msg);
                out.flush();
            }
        } catch (IOException e) { 
        }
    }

    private void sendError(String header, String content) {
        try {
            synchronized (out) {
                out.writeUTF(header);
                out.writeUTF(content);
                out.flush();
            }
        } catch (IOException e) {}
    }

    private byte[] readByteArray() throws IOException {
        int length = in.readInt();
        byte[] buffer = new byte[length];
        in.readFully(buffer);
        return buffer;
    }

    private void closeConnection() {
        if (!active) return; 
        active = false;
        
        Servidor.removeClient(this);

        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
        }
    }
}