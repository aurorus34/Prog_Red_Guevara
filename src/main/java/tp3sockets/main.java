package tp3sockets;

import java.util.Scanner;

//Clase principal que permite ejecutar tanto el servidor como el cliente
public class main {
    public static void main(String[] args) {
        mostrarMenuPrincipal();
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = scanner.nextInt();
                
                switch (opcion) {
                    case 1:
                        ConsoleColors.printInfo("Iniciando servidor...");
                        System.out.println();
                        ServidorMain.main(new String[]{});
                        break;
                        
                    case 2:
                        ConsoleColors.printInfo("Iniciando cliente...");
                        System.out.println();
                        ClienteMain.main(new String[]{});
                        break;
                        
                    case 3:
                        ConsoleColors.printSuccess("¡Hasta luego!");
                        System.exit(0);
                        break;
                        
                    default:
                        ConsoleColors.printError("Opción inválida. Por favor seleccione 1, 2 o 3.");
                        break;
                }
                
            } catch (Exception e) {
                ConsoleColors.printError("Entrada inválida. Por favor ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer
            }
            
            // Pausa antes de mostrar el menú nuevamente
            System.out.println();
            ConsoleColors.printInfo("Presione Enter para continuar...");
            try {
                System.in.read();
            } catch (Exception e) {
                // Ignorar excepciones de entrada
            }
            
            // Limpiar pantalla (simulado)
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    //Muestra el menú principal de la aplicación
    private static void mostrarMenuPrincipal() {
        System.out.println();
        ConsoleColors.printSuccess("╔═══════════════════════════════════════════════════╗");
        ConsoleColors.printSuccess("║         SISTEMA DE TRANSMISIÓN DE ARCHIVOS       ║");
        ConsoleColors.printSuccess("║                   TCP SOCKETS                     ║");
        ConsoleColors.printSuccess("╚═══════════════════════════════════════════════════╝");
        System.out.println();
        
        ConsoleColors.printInfo("Características del sistema:");
        ConsoleColors.printInfo("• Comunicación cliente-servidor mediante sockets TCP");
        ConsoleColors.printInfo("• Selección de archivos con interfaz gráfica");
        ConsoleColors.printInfo("• Transferencia por bloques de 4KB");
        ConsoleColors.printInfo("• Múltiples archivos por sesión");
        ConsoleColors.printInfo("• Mensajes con colores en consola");
        ConsoleColors.printInfo("• Manejo robusto de errores");
        System.out.println();
        
        ConsoleColors.printWarning("┌─────────────────────────────────────────┐");
        ConsoleColors.printWarning("│              MENÚ PRINCIPAL             │");
        ConsoleColors.printWarning("├─────────────────────────────────────────┤");
        ConsoleColors.printWarning("│  1. Ejecutar Servidor                   │");
        ConsoleColors.printWarning("│  2. Ejecutar Cliente                    │");
        ConsoleColors.printWarning("│  3. Salir                               │");
        ConsoleColors.printWarning("└─────────────────────────────────────────┘");
        System.out.println();
        
        ConsoleColors.printInfo("Instrucciones:");
        ConsoleColors.printInfo("1. Primero ejecute el SERVIDOR (opción 1)");
        ConsoleColors.printInfo("2. En otra terminal/consola ejecute el CLIENTE (opción 2)");
        ConsoleColors.printInfo("3. El cliente podrá seleccionar y enviar archivos al servidor");
        System.out.println();
    }
}