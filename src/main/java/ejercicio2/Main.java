package ejercicio2;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Random;


public class Main {
    

    static class Contador implements Runnable {
        private int contador;
        private String nombre;
        private int limite;
        private int tiempoEspera;
        private long tiempoInicio;
        private long tiempoFin;
        

        public Contador(String nombre, int limite, int tiempoEspera) {
            this.contador = 0;
            this.nombre = nombre;
            this.limite = limite;
            this.tiempoEspera = tiempoEspera;
        }
        
        @Override
        public void run() {
            tiempoInicio = System.currentTimeMillis();
            
            try {
                System.out.println(">>> " + nombre + " INICIADO (Límite: " + limite + 
                                   ", Velocidad: " + tiempoEspera + "ms)");
                
                while (contador < limite) {
                    contador++;
                    System.out.println(nombre + ": " + contador);
                    Thread.sleep(tiempoEspera);
                }
                
                tiempoFin = System.currentTimeMillis();
                long tiempoTotal = tiempoFin - tiempoInicio;
                
                System.out.println("<<< " + nombre + " FINALIZADO en " + 
                                   tiempoTotal + "ms");
                
                JOptionPane.showMessageDialog(
                    null,
                    "Contador: " + nombre + "\n" +
                    "Conteo alcanzado: " + contador + "\n" +
                    "Límite: " + limite + "\n" +
                    "Velocidad: " + tiempoEspera + "ms por incremento\n" +
                    "Tiempo total: " + tiempoTotal + "ms (" + 
                    String.format("%.2f", tiempoTotal / 1000.0) + " segundos)",
                    "Contador Finalizado - " + nombre,
                    JOptionPane.INFORMATION_MESSAGE
                );
                
            } catch (InterruptedException e) {
                System.err.println(nombre + " fue interrumpido: " + e.getMessage());
            }
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public long getTiempoTotal() {
            return tiempoFin - tiempoInicio;
        }
    }
    
    public static void main(String[] args) {
        boolean continuar = true;
        Random random = new Random();
        
        while (continuar) {
            int cantidadContadores = random.nextInt(8) + 3;
            
            String mensaje = "Se crearán " + cantidadContadores + " contadores aleatorios.\n\n" +
                           "Cada contador tendrá:\n" +
                           "- Límite aleatorio entre 5 y 20\n" +
                           "- Velocidad aleatoria entre 200ms y 1000ms\n\n" +
                           "¿Desea continuar?";
            
            int opcion = JOptionPane.showConfirmDialog(
                null,
                mensaje,
                "Iniciar Contadores",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (opcion != JOptionPane.YES_OPTION) {
                int confirmacionSalida = JOptionPane.showConfirmDialog(
                    null,
                    "¿Desea salir del programa?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (confirmacionSalida == JOptionPane.YES_OPTION) {
                    continuar = false;
                }
                continue;
            }
            
            ArrayList<Contador> contadores = new ArrayList<>();
            ArrayList<Thread> threads = new ArrayList<>();
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("CREANDO " + cantidadContadores + " CONTADORES");
            System.out.println("=".repeat(60));
            
            for (int i = 0; i < cantidadContadores; i++) {
                String nombre = "Contador-" + (i + 1);
                int limite = random.nextInt(16) + 5; 
                int tiempoEspera = random.nextInt(801) + 200; 
                
                Contador contador = new Contador(nombre, limite, tiempoEspera);
                Thread thread = new Thread(contador);
                
                contadores.add(contador);
                threads.add(thread);
            }
            
            // Mostrar información inicial
            JOptionPane.showMessageDialog(
                null,
                "Se han creado " + cantidadContadores + " contadores.\n\n" +
                "Los contadores se ejecutarán simultáneamente.\n" +
                "Revise la consola para ver el progreso en tiempo real.\n\n" +
                "Al finalizar cada contador, verá una ventana con los resultados.",
                "Contadores Creados",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Iniciar todos los threads al mismo tiempo
            System.out.println("\n>>> INICIANDO TODOS LOS CONTADORES <<<\n");
            for (Thread thread : threads) {
                thread.start();
            }
            
            // Esperar a que todos terminen
            try {
                for (Thread thread : threads) {
                    thread.join();
                }
                
                System.out.println("\n" + "=".repeat(60));
                System.out.println("TODOS LOS CONTADORES HAN FINALIZADO");
                System.out.println("=".repeat(60) + "\n");
                
                // Mostrar resumen final
                StringBuilder resumen = new StringBuilder();
                resumen.append("RESUMEN FINAL:\n");
                resumen.append("=".repeat(40)).append("\n\n");
                
                for (Contador contador : contadores) {
                    resumen.append(contador.getNombre())
                           .append(": ")
                           .append(contador.getTiempoTotal())
                           .append("ms (")
                           .append(String.format("%.2f", contador.getTiempoTotal() / 1000.0))
                           .append(" segundos)\n");
                }
                
                JOptionPane.showMessageDialog(
                    null,
                    resumen.toString(),
                    "Resumen de Ejecución",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error: Uno o más hilos fueron interrumpidos.",
                    "Error de Ejecución",
                    JOptionPane.ERROR_MESSAGE
                );
            }
            
            int repetir = JOptionPane.showConfirmDialog(
                null,
                "¿Desea crear y ejecutar nuevos contadores?",
                "Continuar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (repetir != JOptionPane.YES_OPTION) {
                continuar = false;
            }
        }

        JOptionPane.showMessageDialog(
            null,
            "¡Gracias por usar el programa de contadores!",
            "Programa Finalizado",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}