package tp3sockets;

import java.io.*;
import java.net.Socket;

//Clase encargada de recibir archivos a través de socket
public class FileReceiver {
    private static final int BUFFER_SIZE = 4096; // 4KB buffer
    private static final String RECEIVED_FILES_DIR = "archivos_recibidos";
    

    public static void receiveFile(Socket socket) throws IOException {
        // Crear directorio para archivos recibidos si no existe
        File receivedDir = new File(RECEIVED_FILES_DIR);
        if (!receivedDir.exists()) {
            receivedDir.mkdirs();
            ConsoleColors.printInfo("Directorio creado: " + RECEIVED_FILES_DIR);
        }
        
        try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
            
            // Recibir información del archivo
            String fileName = dataInputStream.readUTF();
            long fileSize = dataInputStream.readLong();
            
            ConsoleColors.printInfo("Recibiendo archivo: " + fileName);
            ConsoleColors.printInfo("Tamaño esperado: " + FileUtils.formatFileSize(fileSize));
            
            // Crear archivo de destino
            File destinationFile = new File(receivedDir, fileName);
            
            // Si el archivo ya existe, crear una versión numerada
            int counter = 1;
            String originalName = fileName;
            String nameWithoutExtension = originalName.contains(".") ? 
                originalName.substring(0, originalName.lastIndexOf(".")) : originalName;
            String extension = originalName.contains(".") ? 
                originalName.substring(originalName.lastIndexOf(".")) : "";
                
            while (destinationFile.exists()) {
                fileName = nameWithoutExtension + "(" + counter + ")" + extension;
                destinationFile = new File(receivedDir, fileName);
                counter++;
            }
            
            if (!originalName.equals(fileName)) {
                ConsoleColors.printWarning("Archivo renombrado a: " + fileName);
            }
            
            try (
                FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
            ) {
                byte[] buffer = new byte[BUFFER_SIZE];
                long totalBytesReceived = 0;
                int bytesRead;
                
                while (totalBytesReceived < fileSize) {
                    int bytesToRead = (int) Math.min(BUFFER_SIZE, fileSize - totalBytesReceived);
                    bytesRead = dataInputStream.read(buffer, 0, bytesToRead);
                    
                    if (bytesRead == -1) {
                        throw new IOException("Conexión cerrada inesperadamente");
                    }
                    
                    bufferedOutputStream.write(buffer, 0, bytesRead);
                    totalBytesReceived += bytesRead;
                    
                    // Mostrar progreso cada 100KB aproximadamente
                    if (totalBytesReceived % (100 * 1024) == 0 || totalBytesReceived == fileSize) {
                        double percentage = (double) totalBytesReceived / fileSize * 100;
                        ConsoleColors.printInfo(String.format("Progreso: %.1f%% (%s / %s)", 
                            percentage, 
                            FileUtils.formatFileSize(totalBytesReceived), 
                            FileUtils.formatFileSize(fileSize)
                        ));
                    }
                }
                
                bufferedOutputStream.flush();
                ConsoleColors.printSuccess("✓ Archivo recibido exitosamente: " + fileName);
                ConsoleColors.printSuccess("✓ Guardado en: " + destinationFile.getAbsolutePath());
                
            } catch (IOException e) {
                // Si hay error, intentar eliminar el archivo parcialmente creado
                if (destinationFile.exists()) {
                    destinationFile.delete();
                }
                throw e;
            }
            
        } catch (IOException e) {
            ConsoleColors.printError("✗ Error al recibir el archivo: " + e.getMessage());
            throw e;
        }
    }
}