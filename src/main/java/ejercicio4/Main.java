package ejercicio4;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    
    private static final int TAMANIO = 4;
    private static int[][] matrizA = new int[TAMANIO][TAMANIO];
    private static int[][] matrizB = new int[TAMANIO][TAMANIO];
    private static int[][] matrizResultadoSecuencial = new int[TAMANIO][TAMANIO];
    private static int[][] matrizResultadoParalelo = new int[TAMANIO][TAMANIO];
    

    static class CalculadorFila implements Runnable {
        private int fila;
        private long tiempoInicio;
        private long tiempoFin;
        
        public CalculadorFila(int fila) {
            this.fila = fila;
        }
        
        @Override
        public void run() {
            tiempoInicio = System.nanoTime();
            
            // Calcular la fila asignada
            for (int j = 0; j < TAMANIO; j++) {
                matrizResultadoParalelo[fila][j] = 0;
                for (int k = 0; k < TAMANIO; k++) {
                    matrizResultadoParalelo[fila][j] += matrizA[fila][k] * matrizB[k][j];
                }
            }
            
            tiempoFin = System.nanoTime();
        }
        
        public long getTiempoEjecucion() {
            return tiempoFin - tiempoInicio;
        }
        
        public int getFila() {
            return fila;
        }
    }
    
    /**
     * Genera matrices aleatorias
     */
    private static void generarMatricesAleatorias() {
        Random random = new Random();
        
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                matrizA[i][j] = random.nextInt(10); // Valores entre 0 y 9
                matrizB[i][j] = random.nextInt(10);
            }
        }
    }
    

    private static boolean ingresarMatricesManualmente(JFrame parent) {
        // Ingresar Matriz A
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                while (true) {
                    String input = JOptionPane.showInputDialog(
                        parent,
                        "Matriz A [" + i + "][" + j + "]:",
                        "Ingreso de Matriz A",
                        JOptionPane.QUESTION_MESSAGE
                    );
                    
                    if (input == null) {
                        int confirmacion = JOptionPane.showConfirmDialog(
                            parent,
                            "¿Desea cancelar el ingreso de datos?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION
                        );
                        if (confirmacion == JOptionPane.YES_OPTION) {
                            return false;
                        }
                        continue;
                    }
                    
                    try {
                        matrizA[i][j] = Integer.parseInt(input.trim());
                        break;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                            parent,
                            "Error: Debe ingresar un número entero válido.",
                            "Entrada Inválida",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        }
        
        // Ingresar Matriz B
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                while (true) {
                    String input = JOptionPane.showInputDialog(
                        parent,
                        "Matriz B [" + i + "][" + j + "]:",
                        "Ingreso de Matriz B",
                        JOptionPane.QUESTION_MESSAGE
                    );
                    
                    if (input == null) {
                        int confirmacion = JOptionPane.showConfirmDialog(
                            parent,
                            "¿Desea cancelar el ingreso de datos?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION
                        );
                        if (confirmacion == JOptionPane.YES_OPTION) {
                            return false;
                        }
                        continue;
                    }
                    
                    try {
                        matrizB[i][j] = Integer.parseInt(input.trim());
                        break;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                            parent,
                            "Error: Debe ingresar un número entero válido.",
                            "Entrada Inválida",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        }
        
        return true;
    }
    

    private static long calcularProductoSecuencial() {
        long inicio = System.nanoTime();
        
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                matrizResultadoSecuencial[i][j] = 0;
                for (int k = 0; k < TAMANIO; k++) {
                    matrizResultadoSecuencial[i][j] += matrizA[i][k] * matrizB[k][j];
                }
            }
        }
        
        long fin = System.nanoTime();
        return fin - inicio;
    }
    

    private static long calcularProductoParalelo(CalculadorFila[] calculadores) {
        long inicio = System.nanoTime();
        
        Thread[] threads = new Thread[TAMANIO];
        
        // Crear y arrancar los 4 threads
        for (int i = 0; i < TAMANIO; i++) {
            calculadores[i] = new CalculadorFila(i);
            threads[i] = new Thread(calculadores[i]);
            threads[i].start();
        }
        
        // Esperar a que todos terminen
        try {
            for (int i = 0; i < TAMANIO; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        long fin = System.nanoTime();
        return fin - inicio;
    }
    

    private static String matrizToString(int[][] matriz) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAMANIO; i++) {
            sb.append("[ ");
            for (int j = 0; j < TAMANIO; j++) {
                sb.append(String.format("%4d", matriz[i][j]));
                if (j < TAMANIO - 1) sb.append(", ");
            }
            sb.append(" ]\n");
        }
        return sb.toString();
    }
    

    private static void mostrarResultados(JFrame parent, String titulo, String contenido) {
        JDialog dialog = new JDialog(parent, titulo, true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(parent);
        
        JTextArea textArea = new JTextArea(contenido);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());
        
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(panelBoton, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Producto de Matrices 4x4");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            frame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel lblTitulo = new JLabel("Producto de Matrices - Secuencial vs Paralelo", JLabel.CENTER);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
            
            JButton btnAleatorio = new JButton("Generar Matrices Aleatorias y Calcular");
            JButton btnManual = new JButton("Ingresar Matrices Manualmente y Calcular");
            JButton btnComparar = new JButton("Ver Comparación de Resultados");
            JButton btnSalir = new JButton("Salir");
            
            btnComparar.setEnabled(false);
            
            panel.add(lblTitulo);
            panel.add(btnAleatorio);
            panel.add(btnManual);
            panel.add(btnComparar);
            panel.add(btnSalir);
            
            frame.add(panel);
            
            // Acción: Generar aleatorias
            btnAleatorio.addActionListener(e -> {
                generarMatricesAleatorias();
                
                // Mostrar matrices generadas
                String matricesGeneradas = "═══════════ MATRICES GENERADAS ═══════════\n\n" +
                    "MATRIZ A:\n" + matrizToString(matrizA) + "\n" +
                    "MATRIZ B:\n" + matrizToString(matrizB);
                
                mostrarResultados(frame, "Matrices Generadas", matricesGeneradas);
                
                // Calcular secuencial
                long tiempoSecuencial = calcularProductoSecuencial();
                
                StringBuilder resultadoSecuencial = new StringBuilder();
                resultadoSecuencial.append("═══════════ CÁLCULO SECUENCIAL ═══════════\n\n");
                resultadoSecuencial.append("Tiempo de ejecución: ")
                    .append(String.format("%.6f", tiempoSecuencial / 1_000_000.0))
                    .append(" ms\n");
                resultadoSecuencial.append("Tiempo de ejecución: ")
                    .append(tiempoSecuencial)
                    .append(" nanosegundos\n\n");
                resultadoSecuencial.append("MATRIZ RESULTADO:\n")
                    .append(matrizToString(matrizResultadoSecuencial));
                
                mostrarResultados(frame, "Resultado Secuencial", resultadoSecuencial.toString());
                
                // Calcular paralelo
                CalculadorFila[] calculadores = new CalculadorFila[TAMANIO];
                long tiempoParalelo = calcularProductoParalelo(calculadores);
                
                StringBuilder resultadoParalelo = new StringBuilder();
                resultadoParalelo.append("═══════════ CÁLCULO PARALELO (4 THREADS) ═══════════\n\n");
                resultadoParalelo.append("Tiempo total de ejecución: ")
                    .append(String.format("%.6f", tiempoParalelo / 1_000_000.0))
                    .append(" ms\n");
                resultadoParalelo.append("Tiempo total de ejecución: ")
                    .append(tiempoParalelo)
                    .append(" nanosegundos\n\n");
                
                resultadoParalelo.append("TIEMPOS INDIVIDUALES POR THREAD:\n");
                for (int i = 0; i < TAMANIO; i++) {
                    resultadoParalelo.append("Thread ").append(i).append(" (Fila ").append(i).append("): ")
                        .append(String.format("%.6f", calculadores[i].getTiempoEjecucion() / 1_000_000.0))
                        .append(" ms (")
                        .append(calculadores[i].getTiempoEjecucion())
                        .append(" ns)\n");
                }
                
                resultadoParalelo.append("\nRESULTADO DE CADA THREAD:\n");
                for (int i = 0; i < TAMANIO; i++) {
                    resultadoParalelo.append("Fila ").append(i).append(": [ ");
                    for (int j = 0; j < TAMANIO; j++) {
                        resultadoParalelo.append(String.format("%4d", matrizResultadoParalelo[i][j]));
                        if (j < TAMANIO - 1) resultadoParalelo.append(", ");
                    }
                    resultadoParalelo.append(" ]\n");
                }
                
                resultadoParalelo.append("\nMATRIZ RESULTADO COMPLETA:\n")
                    .append(matrizToString(matrizResultadoParalelo));
                
                mostrarResultados(frame, "Resultado Paralelo", resultadoParalelo.toString());
                
                btnComparar.setEnabled(true);
            });
            
            // Acción: Ingresar manual
            btnManual.addActionListener(e -> {
                if (ingresarMatricesManualmente(frame)) {
                    // Calcular secuencial
                    long tiempoSecuencial = calcularProductoSecuencial();
                    
                    StringBuilder resultadoSecuencial = new StringBuilder();
                    resultadoSecuencial.append("═══════════ CÁLCULO SECUENCIAL ═══════════\n\n");
                    resultadoSecuencial.append("MATRIZ A:\n").append(matrizToString(matrizA)).append("\n");
                    resultadoSecuencial.append("MATRIZ B:\n").append(matrizToString(matrizB)).append("\n");
                    resultadoSecuencial.append("Tiempo de ejecución: ")
                        .append(String.format("%.6f", tiempoSecuencial / 1_000_000.0))
                        .append(" ms\n");
                    resultadoSecuencial.append("Tiempo de ejecución: ")
                        .append(tiempoSecuencial)
                        .append(" nanosegundos\n\n");
                    resultadoSecuencial.append("MATRIZ RESULTADO:\n")
                        .append(matrizToString(matrizResultadoSecuencial));
                    
                    mostrarResultados(frame, "Resultado Secuencial", resultadoSecuencial.toString());
                    
                    // Calcular paralelo
                    CalculadorFila[] calculadores = new CalculadorFila[TAMANIO];
                    long tiempoParalelo = calcularProductoParalelo(calculadores);
                    
                    StringBuilder resultadoParalelo = new StringBuilder();
                    resultadoParalelo.append("═══════════ CÁLCULO PARALELO (4 THREADS) ═══════════\n\n");
                    resultadoParalelo.append("Tiempo total de ejecución: ")
                        .append(String.format("%.6f", tiempoParalelo / 1_000_000.0))
                        .append(" ms\n");
                    resultadoParalelo.append("Tiempo total de ejecución: ")
                        .append(tiempoParalelo)
                        .append(" nanosegundos\n\n");
                    
                    resultadoParalelo.append("TIEMPOS INDIVIDUALES POR THREAD:\n");
                    for (int i = 0; i < TAMANIO; i++) {
                        resultadoParalelo.append("Thread ").append(i).append(" (Fila ").append(i).append("): ")
                            .append(String.format("%.6f", calculadores[i].getTiempoEjecucion() / 1_000_000.0))
                            .append(" ms (")
                            .append(calculadores[i].getTiempoEjecucion())
                            .append(" ns)\n");
                    }
                    
                    resultadoParalelo.append("\nRESULTADO DE CADA THREAD:\n");
                    for (int i = 0; i < TAMANIO; i++) {
                        resultadoParalelo.append("Fila ").append(i).append(": [ ");
                        for (int j = 0; j < TAMANIO; j++) {
                            resultadoParalelo.append(String.format("%4d", matrizResultadoParalelo[i][j]));
                            if (j < TAMANIO - 1) resultadoParalelo.append(", ");
                        }
                        resultadoParalelo.append(" ]\n");
                    }
                    
                    resultadoParalelo.append("\nMATRIZ RESULTADO COMPLETA:\n")
                        .append(matrizToString(matrizResultadoParalelo));
                    
                    mostrarResultados(frame, "Resultado Paralelo", resultadoParalelo.toString());
                    
                    btnComparar.setEnabled(true);
                }
            });
            
            // Acción: Comparar
            btnComparar.addActionListener(e -> {
                // Recalcular para tener tiempos actualizados
                long tiempoSecuencial = calcularProductoSecuencial();
                CalculadorFila[] calculadores = new CalculadorFila[TAMANIO];
                long tiempoParalelo = calcularProductoParalelo(calculadores);
                
                StringBuilder comparacion = new StringBuilder();
                comparacion.append("═══════════ COMPARACIÓN DE RENDIMIENTO ═══════════\n\n");
                
                comparacion.append("MÉTODO SECUENCIAL:\n");
                comparacion.append("  Tiempo: ").append(String.format("%.6f", tiempoSecuencial / 1_000_000.0))
                    .append(" ms (").append(tiempoSecuencial).append(" ns)\n\n");
                
                comparacion.append("MÉTODO PARALELO (4 Threads):\n");
                comparacion.append("  Tiempo: ").append(String.format("%.6f", tiempoParalelo / 1_000_000.0))
                    .append(" ms (").append(tiempoParalelo).append(" ns)\n\n");
                
                double mejora = ((double) tiempoSecuencial / tiempoParalelo);
                comparacion.append("ANÁLISIS:\n");
                if (tiempoSecuencial > tiempoParalelo) {
                    comparacion.append("  ✓ El método paralelo es más rápido\n");
                    comparacion.append("  ✓ Mejora de velocidad: ").append(String.format("%.2fx", mejora)).append("\n");
                    comparacion.append("  ✓ Reducción de tiempo: ")
                        .append(String.format("%.2f%%", ((tiempoSecuencial - tiempoParalelo) * 100.0 / tiempoSecuencial)))
                        .append("\n");
                } else {
                    comparacion.append("  ✓ El método secuencial es más rápido\n");
                    comparacion.append("  ✓ Nota: En matrices pequeñas (4x4), el overhead de crear\n");
                    comparacion.append("    threads puede ser mayor que el beneficio del paralelismo.\n");
                }
                
                comparacion.append("\nVERIFICACIÓN:\n");
                boolean sonIguales = true;
                for (int i = 0; i < TAMANIO && sonIguales; i++) {
                    for (int j = 0; j < TAMANIO && sonIguales; j++) {
                        if (matrizResultadoSecuencial[i][j] != matrizResultadoParalelo[i][j]) {
                            sonIguales = false;
                        }
                    }
                }
                
                if (sonIguales) {
                    comparacion.append("  ✓ Ambos métodos produjeron el mismo resultado\n");
                    comparacion.append("  ✓ Cálculo verificado correctamente\n");
                } else {
                    comparacion.append("  ✗ ERROR: Los resultados son diferentes\n");
                }
                
                mostrarResultados(frame, "Comparación de Rendimiento", comparacion.toString());
            });
            
            // Acción: Salir
            btnSalir.addActionListener(e -> {
                int confirmacion = JOptionPane.showConfirmDialog(
                    frame,
                    "¿Está seguro que desea salir?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            });
            
            frame.setVisible(true);
            
            JOptionPane.showMessageDialog(
                frame,
                "Bienvenido al Calculador de Producto de Matrices\n\n" +
                "Este programa calcula el producto de dos matrices 4x4\n" +
                "utilizando dos métodos:\n\n" +
                "1. SECUENCIAL: Cálculo tradicional en un solo hilo\n" +
                "2. PARALELO: Cálculo usando 4 threads (uno por fila)\n\n" +
                "Puede generar matrices aleatorias o ingresarlas manualmente.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}