package TP4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Cliente {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3500;
    
    private static final String SECRET_PHRASE = "Consortini Gonzalini :3";
    
    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private boolean running = false;
    private final Scanner userInput;

    public Cliente() {
        this.userInput = new Scanner(System.in);
    }

    public void start() {
        try {
            connectToServer();
            handleLogin();
            
            // Iniciar hilo de escucha
            Thread listenerThread = new Thread(this::listenFromServer, "ListenerThread");
            listenerThread.start();
            
            // Ciclo principal de envío
            inputLoop();

        } catch (Exception e) {
            System.err.println("Error crítico en el cliente: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void connectToServer() throws IOException {
        socket = new Socket(HOST, PORT);
        running = true;
        inStream = new DataInputStream(socket.getInputStream());
        outStream = new DataOutputStream(socket.getOutputStream());
    }

    private void handleLogin() throws IOException {
        System.out.print("Ingrese su ID: ");
        String username = userInput.nextLine();
        if (username == null || username.trim().isEmpty()) {
            username = "anon";
        }
        outStream.writeUTF(username);
        outStream.flush();
        System.out.println("Bienvenido al chat " + username);
        printPrompt();
    }

    private void inputLoop() {
        try {
            while (running) {
                String line = userInput.nextLine();
                if (line == null || line.trim().isEmpty()) {
                    printPrompt();
                    continue;
                }

                line = line.trim();
                
                if (line.startsWith("/")) {
                    processCommand(line);
                } else {
                    sendBroadcastMessage(line);
                }
                printPrompt();
            }
        } catch (Exception e) {
            if (running) e.printStackTrace();
        }
    }

    private void processCommand(String line) throws IOException {
        String[] tokens = line.split(" ", 3);
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "/salir":
                outStream.writeUTF("/salir");
                outStream.flush();
                running = false;
                break;
            case "/listar":
                outStream.writeUTF("/listar");
                outStream.flush();
                break;
            case "/vercomandos":
            case "/ayuda":
                printHelp();
                break;
            case "/msg":
                if (tokens.length < 3) {
                    printError("Uso correcto: /msg <usuario> <mensaje>");
                } else {
                    sendPrivateMessage(tokens[1], tokens[2]);
                }
                break;
            case "/enviararchivo":
                if (tokens.length < 2) {
                    printError("Uso correcto: /enviarArchivo <usuario> [ruta]");
                } else {
                    String path = (tokens.length == 3) ? tokens[2] : null;
                    if (path == null || path.isBlank()) {
                        printError("Debe especificar la ruta del archivo.");
                    } else {
                        sendFile(tokens[1], path);
                    }
                }
                break;
            default:
                printError("Comando no reconocido.");
        }
    }

    private void listenFromServer() {
        try {
            while (running && !socket.isClosed()) {
                String header = inStream.readUTF();
                
                switch (header) {
                    case "RESP::LIST":
                        System.out.println(Utils.COLORES[3] + "Usuarios conectados: " + inStream.readUTF() + Utils.RESET);
                        break;
                    case "RESP::NOTFOUND":
                        printError("Usuario no encontrado: " + inStream.readUTF());
                        break;
                    case "INCOMING::PRIVATE":
                    case "INCOMING::BROADCAST":
                        handleIncomingEncryptedMessage(header, inStream.readUTF());
                        break;
                    default:
                        System.out.println(Utils.COLORES[0] + header + Utils.RESET);
                }
                printPrompt();
            }
        } catch (IOException e) {
            if (running) System.out.println("\nDesconectado del servidor.");
        }
    }

    private void handleIncomingEncryptedMessage(String type, String sender) throws IOException {
        try {
            int ivSize = inStream.readInt();
            byte[] iv = new byte[ivSize];
            inStream.readFully(iv);

            int contentSize = inStream.readInt();
            byte[] encryptedContent = new byte[contentSize];
            inStream.readFully(encryptedContent);

            String contentType = inStream.readUTF();
            String extraData = inStream.readUTF();

            // Desencriptación
            byte[] decrypted = Utils.decriptarBytes(SECRET_PHRASE, iv, encryptedContent);

            if (decrypted == null) {
                printError("Fallo al desencriptar mensaje de " + sender);
                return;
            }

            if ("TEXT".equals(contentType)) {
                String msgContent = new String(decrypted, StandardCharsets.UTF_8);
                String prefix = type.equals("INCOMING::PRIVATE") ? "[PRIVADO de " : "[";
                System.out.println(Utils.COLORES[2] + prefix + sender + "] " + msgContent + Utils.RESET);
            } else if ("FILE".equals(contentType)) {
                saveIncomingFile(decrypted, extraData, sender);
            }

        } catch (Exception e) {
            printError("Error procesando mensaje entrante: " + e.getMessage());
        }
    }

    private void saveIncomingFile(byte[] data, String suggestedName, String sender) {
        File folder = new File("recibidos");
        if (!folder.exists()) folder.mkdirs();

        String fileName = (suggestedName != null) ? suggestedName : "downloaded_file";
        File targetFile = new File(folder, fileName);
        
        int counter = 1;
        String baseName = fileName;
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if(dotIndex > 0) {
            baseName = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        }

        while(targetFile.exists()) {
            targetFile = new File(folder, baseName + "_" + counter + extension);
            counter++;
        }

        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            fos.write(data);
            System.out.println(Utils.COLORES[0] + "Archivo recibido de " + sender + " guardado en: " + targetFile.getPath() + Utils.RESET);
        } catch (IOException e) {
            printError("No se pudo guardar el archivo: " + e.getMessage());
        }
    }

    private void sendPrivateMessage(String recipient, String message) {
        try {
            byte[] rawData = message.getBytes(StandardCharsets.UTF_8);
            sendEncryptedPacket("FORWARD::PRIVATE", recipient, rawData, "TEXT", "");
            System.out.println(Utils.COLORES[0] + "Mensaje enviado a " + recipient + Utils.RESET);
        } catch (Exception e) {
            printError("Error enviando mensaje: " + e.getMessage());
        }
    }

    private void sendBroadcastMessage(String message) {
        try {
            byte[] rawData = message.getBytes(StandardCharsets.UTF_8);
            sendEncryptedPacket("FORWARD::BROADCAST", null, rawData, "TEXT", "");
        } catch (Exception e) {
            printError("Error enviando broadcast: " + e.getMessage());
        }
    }

    private void sendFile(String recipient, String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            printError("El archivo no existe.");
            return;
        }
        if (file.length() > 10 * 1024 * 1024) {
            printError("El archivo excede el límite de 10MB.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileData = fis.readAllBytes();
            sendEncryptedPacket("FORWARD::PRIVATE", recipient, fileData, "FILE", file.getName());
            System.out.println(Utils.COLORES[0] + "Archivo enviado exitosamente a " + recipient + Utils.RESET);
        } catch (Exception e) {
            printError("Error leyendo/enviando archivo: " + e.getMessage());
        }
    }

    // Método helper para evitar repetir la lógica de encriptación y envío
    private void sendEncryptedPacket(String header, String target, byte[] data, String type, String extra) throws Exception {
        byte[] iv = new byte[16];
        Utils.sr.nextBytes(iv);
        
        byte[] encryptedData = Utils.encriptarBytes(SECRET_PHRASE, iv, data);

        synchronized (outStream) {
            outStream.writeUTF(header);
            if (target != null) {
                outStream.writeUTF(target);
            }
            outStream.writeInt(iv.length);
            outStream.write(iv);
            outStream.writeInt(encryptedData.length);
            outStream.write(encryptedData);
            outStream.writeUTF(type);
            outStream.writeUTF(extra);
            outStream.flush();
        }
    }

    private void disconnect() {
        running = false;
        try {
            if (outStream != null) outStream.close();
            if (socket != null) socket.close();
            if (userInput != null) userInput.close();
        } catch (IOException e) {
            // Ignorar errores al cerrar
        }
    }

    private void printHelp() {
        System.out.println(Utils.COLORES[3] + "--- Ayuda del Chat ---" + Utils.RESET);
        System.out.println(" /listar                      -> Ver conectados");
        System.out.println(" /msg <user> <txt>            -> Mensaje privado");
        System.out.println(" /enviarArchivo <user> <path> -> Transferir archivo");
        System.out.println(" /salir                       -> Cerrar sesión");
    }

    private void printError(String msg) {
        System.out.println(Utils.COLORES[4] + "Error: " + msg + Utils.RESET);
    }

    private void printPrompt() {
        System.out.print("\t-> ");
    }

    public static void main(String[] args) {
        new Cliente().start();
    }
}