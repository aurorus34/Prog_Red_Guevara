package tp3sockets;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.ConnectException;
import java.net.UnknownHostException;


public class ClienteMain {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;
    
    public static void main(String[] args) {
        ClienteUI.mostrarMenuPrincipal();
        
        Socket socket = null;
        boolean continuarEnviando = true;
        
        try {
            // Intentar conectar al servidor
            ClienteLogger.logInfo("Intentando conectar al servidor en " + SERVER_HOST + ":" + SERVER_PORT);
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            ClienteLogger.logSuccess("✓ Conexión establecida con el servidor");
            System.out.println();
            
            // Ciclo principal para envío de múltiples archivos
            while (continuarEnviando) {
                // Solicitar archivo al usuario
                File archivoSeleccionado = ClienteUI.solicitarArchivo();
                
                if (archivoSeleccionado == null) {
                    ClienteLogger.logWarning("No se seleccionó ningún archivo");
                    continuarEnviando = ClienteUI.preguntarContinuar();
                    continue;
                }
                
                // Verificar que el archivo existe y es legible
                if (!archivoSeleccionado.exists()) {
                    ClienteLogger.logError("El archivo seleccionado no existe");
                    continuarEnviando = ClienteUI.preguntarContinuar();
                    continue;
                }
                
                if (!archivoSeleccionado.canRead()) {
                    ClienteLogger.logError("No se puede leer el archivo seleccionado");
                    continuarEnviando = ClienteUI.preguntarContinuar();
                    continue;
                }
                
                if (archivoSeleccionado.length() == 0) {
                    ClienteLogger.logWarning("El archivo seleccionado está vacío");
                    continuarEnviando = ClienteUI.preguntarContinuar();
                    continue;
                }
                
                try {
                    // Enviar el archivo
                    System.out.println();
                    ClienteLogger.logInfo("Iniciando transmisión del archivo...");
                    FileSender.sendFile(socket, archivoSeleccionado);
                    System.out.println();
                    
                    // Preguntar si desea enviar otro archivo
                    continuarEnviando = ClienteUI.preguntarContinuar();
                    
                } catch (IOException e) {
                    ClienteLogger.logError("Error durante la transmisión: " + e.getMessage());
                    continuarEnviando = false; // Terminar en caso de error de transmisión
                }
            }
            
        } catch (ConnectException e) {
            ClienteUI.mostrarErrorConexion("Servidor no disponible - " + e.getMessage());
        } catch (UnknownHostException e) {
            ClienteUI.mostrarErrorConexion("Host desconocido - " + e.getMessage());
        } catch (IOException e) {
            ClienteLogger.logError("Error de conexión: " + e.getMessage());
        } finally {
            // Cerrar la conexión
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                    ClienteLogger.logInfo("Conexión cerrada correctamente");
                } catch (IOException e) {
                    ClienteLogger.logError("Error al cerrar la conexión: " + e.getMessage());
                }
            }
            
            ClienteUI.mostrarDespedida();
        }
    }
}