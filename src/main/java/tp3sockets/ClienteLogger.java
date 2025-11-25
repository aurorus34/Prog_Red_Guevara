package tp3sockets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ClienteLogger {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
 
    public static void logInfo(String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        ConsoleColors.printInfo("[" + timestamp + "] CLIENTE: " + message);
    }
    

    public static void logSuccess(String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        ConsoleColors.printSuccess("[" + timestamp + "] CLIENTE: " + message);
    }
    

    public static void logError(String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        ConsoleColors.printError("[" + timestamp + "] CLIENTE: " + message);
    }
    

    public static void logWarning(String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        ConsoleColors.printWarning("[" + timestamp + "] CLIENTE: " + message);
    }
}