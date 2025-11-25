package tp3sockets;

import javax.swing.JOptionPane;
import java.io.File;

//Clase para la interfaz de usuario del cliente
public class ClienteUI {
    
    ///Muestra el menú principal del cliente
    public static void mostrarMenuPrincipal() {
        ConsoleColors.printInfo("=== CLIENTE DE TRANSMISIÓN DE ARCHIVOS ===");
        ConsoleColors.printInfo("Preparado para conectarse al servidor...");
        System.out.println();
    }
    

    public static File solicitarArchivo() {
        ClienteLogger.logInfo("Abriendo selector de archivos...");
        File selectedFile = FileUtils.selectFile();
        
        if (selectedFile == null) {
            ClienteLogger.logWarning("Selección de archivo cancelada por el usuario");
        } else {
            ClienteLogger.logSuccess("Archivo seleccionado: " + selectedFile.getName());
            ClienteLogger.logInfo("Ruta: " + selectedFile.getAbsolutePath());
            ClienteLogger.logInfo("Tamaño: " + FileUtils.formatFileSize(selectedFile.length()));
        }
        
        return selectedFile;
    }
    

    public static boolean preguntarContinuar() {
        ClienteLogger.logInfo("Preguntando al usuario si desea continuar...");
        boolean continuar = FileUtils.askForContinue();
        
        if (continuar) {
            ClienteLogger.logInfo("El usuario eligió enviar otro archivo");
        } else {
            ClienteLogger.logInfo("El usuario eligió finalizar la transmisión");
        }
        
        return continuar;
    }
    

    public static void mostrarDespedida() {
        System.out.println();
        ConsoleColors.printSuccess("=== TRANSMISIÓN FINALIZADA ===");
        ConsoleColors.printInfo("Gracias por usar el cliente de transmisión de archivos");
        System.out.println();
    }
    

    public static void mostrarErrorConexion(String mensaje) {
        ConsoleColors.printError("═══ ERROR DE CONEXIÓN ═══");
        ConsoleColors.printError("No se pudo conectar al servidor:");
        ConsoleColors.printError(mensaje);
        ConsoleColors.printInfo("Verifique que el servidor esté ejecutándose");
        System.out.println();
    }
}