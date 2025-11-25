package tp3sockets;

import java.io.*;
import java.net.Socket;


public class FileSender {
    private static final int BUFFER_SIZE = 4096; // 4KB buffer
    

    public static void sendFile(Socket socket, File file) throws IOException {
        ConsoleColors.printInfo("Iniciando envío del archivo: " + file.getName());
        ConsoleColors.printInfo("Tamaño del archivo: " + FileUtils.formatFileSize(file.length()));
        
        try (
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            // Enviar información del archivo primero
            dataOutputStream.writeUTF(file.getName()); // Nombre del archivo
            dataOutputStream.writeLong(file.length()); // Tamaño del archivo
            dataOutputStream.flush();
            
            // Enviar el contenido del archivo
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            long totalBytesSent = 0;
            
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);
                totalBytesSent += bytesRead;
                
                // Mostrar progreso cada 100KB aproximadamente
                if (totalBytesSent % (100 * 1024) == 0 || totalBytesSent == file.length()) {
                    double percentage = (double) totalBytesSent / file.length() * 100;
                    ConsoleColors.printInfo(String.format("Progreso: %.1f%% (%s / %s)", 
                        percentage, 
                        FileUtils.formatFileSize(totalBytesSent), 
                        FileUtils.formatFileSize(file.length())
                    ));
                }
            }
            
            dataOutputStream.flush();
            ConsoleColors.printSuccess("✓ Archivo enviado exitosamente: " + file.getName());
            
        } catch (IOException e) {
            ConsoleColors.printError("✗ Error al enviar el archivo: " + e.getMessage());
            throw e;
        }
    }
}