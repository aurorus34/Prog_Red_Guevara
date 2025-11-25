package tp3sockets;


public class ConsoleColors {
    // Códigos ANSI para colores
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    
    // Códigos para texto en negrita
    public static final String BOLD = "\u001B[1m";
    
    // Métodos para imprimir mensajes con colores específicos
    public static void printSuccess(String message) {
        System.out.println(GREEN + BOLD + message + RESET);
    }
    
    public static void printError(String message) {
        System.out.println(RED + BOLD + message + RESET);
    }
    
    public static void printInfo(String message) {
        System.out.println(BLUE + BOLD + message + RESET);
    }
    
    public static void printWarning(String message) {
        System.out.println(YELLOW + BOLD + message + RESET);
    }
}