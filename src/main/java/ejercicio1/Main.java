package ejercicio1;

import javax.swing.JOptionPane;


public class Main {
   
    static class HiloAlfanumerico implements Runnable {
        private int tipo;
        

        public HiloAlfanumerico(int tipo) {
            this.tipo = tipo;
        }
        
        @Override
        public void run() {
            try {
                if (tipo == 1) {
                    mostrarNumeros();
                } else if (tipo == 2) {
                    mostrarLetras();
                }
            } catch (InterruptedException e) {
                System.err.println("El hilo fue interrumpido: " + e.getMessage());
            }
        }
        
        private void mostrarNumeros() throws InterruptedException {
            System.out.println("=== Mostrando números del 1 al 30 ===");
            for (int i = 1; i <= 30; i++) {
                System.out.println("Número: " + i);
                Thread.sleep(100);
            }
            System.out.println("=== Fin de números ===");
        }
        

        private void mostrarLetras() throws InterruptedException {
            System.out.println("=== Mostrando letras de la 'a' a la 'z' ===");
            for (char c = 'a'; c <= 'z'; c++) {
                System.out.println("Letra: " + c);
                Thread.sleep(100);
            }
            System.out.println("=== Fin de letras ===");
        }
    }
    
    public static void main(String[] args) {
        boolean continuar = true;
        
        while (continuar) {
            String input = JOptionPane.showInputDialog(
                null,
                "Ingrese el tipo de hilo a ejecutar:\n\n" +
                "1 - Mostrar números del 1 al 30\n" +
                "2 - Mostrar letras de la 'a' a la 'z'\n" +
                "0 - Salir",
                "Selección de Tipo",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (input == null) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Desea salir del programa?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    continuar = false;
                }
                continue;
            }
            
            if (input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error: Debe ingresar un valor.",
                    "Entrada Inválida",
                    JOptionPane.ERROR_MESSAGE
                );
                continue;
            }
            
            int tipo;
            try {
                tipo = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error: Debe ingresar un número válido (0, 1 o 2).",
                    "Entrada Inválida",
                    JOptionPane.ERROR_MESSAGE
                );
                continue;
            }
            
            if (tipo == 0) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Desea salir del programa?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    continuar = false;
                }
            } else if (tipo == 1 || tipo == 2) {
                HiloAlfanumerico hiloAlfanumerico = new HiloAlfanumerico(tipo);
                Thread hilo = new Thread(hiloAlfanumerico);
                
                String mensaje = tipo == 1 ? 
                    "Iniciando hilo para mostrar números...\nRevise la consola." :
                    "Iniciando hilo para mostrar letras...\nRevise la consola.";
                
                JOptionPane.showMessageDialog(
                    null,
                    mensaje,
                    "Hilo Iniciado",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                hilo.start();
                
                try {
                    hilo.join();
                    JOptionPane.showMessageDialog(
                        null,
                        "El hilo ha finalizado su ejecución.",
                        "Ejecución Completada",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Error: El hilo fue interrumpido.",
                        "Error de Ejecución",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Error: El tipo debe ser 1, 2 o 0 para salir.\n\n" +
                    "1 - Números\n" +
                    "2 - Letras\n" +
                    "0 - Salir",
                    "Tipo Inválido",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        
        JOptionPane.showMessageDialog(
            null,
            "",
            "rograma Finalizado uuwuwu",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}