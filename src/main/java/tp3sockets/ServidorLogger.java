package tp3sockets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ServidorLogger {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    

    public static void logInfo(String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        ConsoleColors.printInfo("[" + timestamp + "] INFO: " + message);
    }
    

    public static void logSuccess(String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        ConsoleColors.printSuccess("[" + timestamp + "] SUCCESS: " + message);
    }
    

    public static void logError(String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        ConsoleColors.printError("[" + timestamp + "] ERROR: " + message);
    }
    

    public static void logWarning(String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        ConsoleColors.printWarning("[" + timestamp + "] WARNING: " + message);
    }
}