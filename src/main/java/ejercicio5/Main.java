package ejercicio5;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;


public class Main {
    
    private static int totalLineas = 0;
    private static final Object lock = new Object();
    

    static class ContadorLineas implements Runnable {
        private File archivo;
        private int lineasContadas;
        private long tiempoInicio;
        private long tiempoFin;
        private boolean exitoso;
        private String error;
        
        public ContadorLineas(File archivo) {
            this.archivo = archivo;
            this.lineasContadas = 0;
            this.exitoso = false;
            this.error = "";
        }
        
        @Override
        public void run() {
            tiempoInicio = System.currentTimeMillis();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    lineasContadas++;
                }
                
                synchronized (lock) {
                    totalLineas += lineasContadas;
                }
                
                exitoso = true;
                System.out.println("✓ Thread [" + Thread.currentThread().getName() + 
                                   "] procesó: " + archivo.getName() + 
                                   " - Líneas: " + lineasContadas);
                
            } catch (IOException e) {
                error = e.getMessage();
                System.err.println("✗ Error procesando " + archivo.getName() + ": " + error);
            } finally {
                tiempoFin = System.currentTimeMillis();
            }
        }
        
        public String getNombreArchivo() {
            return archivo.getName();
        }
        
        public int getLineasContadas() {
            return lineasContadas;
        }
        
        public long getTiempoEjecucion() {
            return tiempoFin - tiempoInicio;
        }
        
        public boolean isExitoso() {
            return exitoso;
        }
        
        public String getError() {
            return error;
        }
    }
    

    private static File[] buscarArchivosTexto(File carpeta) {
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            return new File[0];
        }
        
        return carpeta.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            return lowerName.endsWith(".txt") || 
                   lowerName.endsWith(".text") ||
                   lowerName.endsWith(".log") ||
                   lowerName.endsWith(".csv") ||
                   lowerName.endsWith(".md");
        });
    }
    

    private static File seleccionarCarpeta(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Seleccione una carpeta con archivos de texto");
        
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        int resultado = fileChooser.showOpenDialog(parent);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
    

    private static void crearArchivosPrueba(File carpeta) {
        try {
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            
            File archivo1 = new File(carpeta, "archivo1.txt");
            try (PrintWriter writer = new PrintWriter(archivo1)) {
                writer.println("Línea 1 del archivo 1");
                writer.println("Línea 2 del archivo 1");
                writer.println("Línea 3 del archivo 1");
                writer.println("Línea 4 del archivo 1");
                writer.println("Línea 5 del archivo 1");
            }
            
            File archivo2 = new File(carpeta, "archivo2.txt");
            try (PrintWriter writer = new PrintWriter(archivo2)) {
                writer.println("Primera línea del segundo archivo");
                writer.println("Segunda línea del segundo archivo");
                writer.println("Tercera línea del segundo archivo");
            }
            
            File archivo3 = new File(carpeta, "datos.log");
            try (PrintWriter writer = new PrintWriter(archivo3)) {
                writer.println("Log entry 1");
                writer.println("Log entry 2");
                writer.println("Log entry 3");
                writer.println("Log entry 4");
                writer.println("Log entry 5");
                writer.println("Log entry 6");
                writer.println("Log entry 7");
            }
            
            File archivo4 = new File(carpeta, "notas.md");
            try (PrintWriter writer = new PrintWriter(archivo4)) {
                writer.println("# Título");
                writer.println("## Subtítulo");
                writer.println("Contenido del markdown");
                writer.println("Más contenido");
            }
            
            File archivo5 = new File(carpeta, "lista.csv");
            try (PrintWriter writer = new PrintWriter(archivo5)) {
                writer.println("Nombre,Edad,Ciudad");
                writer.println("Juan,25,Buenos Aires");
                writer.println("María,30,Córdoba");
                writer.println("Pedro,28,Rosario");
                writer.println("Ana,22,Mendoza");
                writer.println("Carlos,35,La Plata");
            }
            
            System.out.println("✓ Archivos de prueba creados exitosamente en: " + carpeta.getAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("✗ Error creando archivos de prueba: " + e.getMessage());
        }
    }
    

    private static void mostrarResultados(JFrame parent, String titulo, String contenido) {
        JDialog dialog = new JDialog(parent, titulo, true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(parent);
        
        JTextArea textArea = new JTextArea(contenido);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        
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
            JFrame frame = new JFrame("Contador de Líneas en Archivos de Texto");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 350);
            frame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel lblTitulo = new JLabel("Contador de Líneas con Threads", JLabel.CENTER);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
            
            JButton btnSeleccionar = new JButton("Seleccionar Carpeta y Procesar");
            JButton btnCrearPrueba = new JButton("Crear Archivos de Prueba");
            JButton btnProcesarPrueba = new JButton("Procesar Carpeta de Prueba");
            JButton btnAyuda = new JButton("Ayuda");
            JButton btnSalir = new JButton("Salir");
            
            panel.add(lblTitulo);
            panel.add(btnSeleccionar);
            panel.add(btnCrearPrueba);
            panel.add(btnProcesarPrueba);
            panel.add(btnAyuda);
            panel.add(btnSalir);
            
            frame.add(panel);
            
            File carpetaPrueba = new File(System.getProperty("user.home"), "ContadorLineasPrueba");
            
            btnSeleccionar.addActionListener(e -> {
                File carpeta = seleccionarCarpeta(frame);
                
                if (carpeta != null) {
                    procesarCarpeta(frame, carpeta);
                }
            });
            
            btnCrearPrueba.addActionListener(e -> {
                crearArchivosPrueba(carpetaPrueba);
                
                JOptionPane.showMessageDialog(
                    frame,
                    "Se han creado 5 archivos de prueba en:\n\n" +
                    carpetaPrueba.getAbsolutePath() + "\n\n" +
                    "Archivos creados:\n" +
                    "- archivo1.txt (5 líneas)\n" +
                    "- archivo2.txt (3 líneas)\n" +
                    "- datos.log (7 líneas)\n" +
                    "- notas.md (4 líneas)\n" +
                    "- lista.csv (6 líneas)\n\n" +
                    "Total esperado: 25 líneas",
                    "Archivos Creados",
                    JOptionPane.INFORMATION_MESSAGE
                );
            });
            
            btnProcesarPrueba.addActionListener(e -> {
                if (!carpetaPrueba.exists() || carpetaPrueba.listFiles() == null || 
                    carpetaPrueba.listFiles().length == 0) {
                    int respuesta = JOptionPane.showConfirmDialog(
                        frame,
                        "La carpeta de prueba no existe o está vacía.\n" +
                        "¿Desea crear archivos de prueba primero?",
                        "Carpeta Vacía",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );
                    
                    if (respuesta == JOptionPane.YES_OPTION) {
                        crearArchivosPrueba(carpetaPrueba);
                    } else {
                        return;
                    }
                }
                
                procesarCarpeta(frame, carpetaPrueba);
            });
            
            btnAyuda.addActionListener(e -> {
                String ayuda = "═══════════ AYUDA - CONTADOR DE LÍNEAS ═══════════\n\n" +
                    "Este programa cuenta las líneas de todos los archivos de texto\n" +
                    "encontrados en una carpeta usando threads paralelos.\n\n" +
                    "TIPOS DE ARCHIVOS SOPORTADOS:\n" +
                    "  • .txt  - Archivos de texto plano\n" +
                    "  • .text - Archivos de texto\n" +
                    "  • .log  - Archivos de registro\n" +
                    "  • .csv  - Archivos CSV\n" +
                    "  • .md   - Archivos Markdown\n\n" +
                    "FUNCIONAMIENTO:\n" +
                    "  1. Se buscan todos los archivos de texto en la carpeta\n" +
                    "  2. Se crea un thread por cada archivo encontrado\n" +
                    "  3. Cada thread cuenta las líneas de su archivo\n" +
                    "  4. Se suma el total de líneas de todos los archivos\n\n" +
                    "BOTONES:\n" +
                    "  • Seleccionar Carpeta: Elige una carpeta para procesar\n" +
                    "  • Crear Archivos de Prueba: Crea 5 archivos de ejemplo\n" +
                    "  • Procesar Carpeta de Prueba: Procesa los archivos de ejemplo\n\n" +
                    "RESULTADOS MOSTRADOS:\n" +
                    "  • Cantidad de archivos encontrados\n" +
                    "  • Cantidad de threads (hilos) trabajando\n" +
                    "  • Líneas contadas por cada archivo\n" +
                    "  • Total de líneas de todos los archivos\n" +
                    "  • Tiempo de ejecución de cada thread";
                
                mostrarResultados(frame, "Ayuda", ayuda);
            });
            
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
                "Bienvenido al Contador de Líneas\n\n" +
                "Este programa procesa archivos de texto usando threads:\n" +
                "• Un thread por cada archivo\n" +
                "• Cuenta todas las líneas\n" +
                "• Muestra resultados detallados\n\n" +
                "Puede crear archivos de prueba o seleccionar su propia carpeta.",
                "Bienvenida",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
    

    private static void procesarCarpeta(JFrame parent, File carpeta) {
        totalLineas = 0;
        
        System.out.println("\n" + "═".repeat(70));
        System.out.println("PROCESANDO CARPETA: " + carpeta.getAbsolutePath());
        System.out.println("═".repeat(70));
        
        File[] archivos = buscarArchivosTexto(carpeta);
        
        if (archivos == null || archivos.length == 0) {
            JOptionPane.showMessageDialog(
                parent,
                "No se encontraron archivos de texto en la carpeta:\n\n" +
                carpeta.getAbsolutePath() + "\n\n" +
                "Tipos de archivo soportados: .txt, .text, .log, .csv, .md",
                "Sin Archivos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        int cantidadArchivos = archivos.length;
        
        System.out.println("Archivos encontrados: " + cantidadArchivos);
        System.out.println("Creando " + cantidadArchivos + " threads...\n");
        
        // Crear threads y contadores
        ArrayList<ContadorLineas> contadores = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < cantidadArchivos; i++) {
            ContadorLineas contador = new ContadorLineas(archivos[i]);
            Thread thread = new Thread(contador, "Thread-" + (i + 1));
            
            contadores.add(contador);
            threads.add(thread);
        }
        
        long tiempoInicio = System.currentTimeMillis();
        System.out.println(">>> INICIANDO PROCESAMIENTO <<<\n");
        
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Esperar a que todos terminen
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Error esperando threads: " + e.getMessage());
        }
        
        long tiempoFin = System.currentTimeMillis();
        long tiempoTotal = tiempoFin - tiempoInicio;
        
        System.out.println("\n>>> PROCESAMIENTO FINALIZADO <<<");
        System.out.println("═".repeat(70) + "\n");
        
        StringBuilder resultados = new StringBuilder();
        resultados.append("═══════════ RESULTADOS DEL PROCESAMIENTO ═══════════\n\n");
        resultados.append("CARPETA PROCESADA:\n");
        resultados.append(carpeta.getAbsolutePath()).append("\n\n");
        resultados.append("═".repeat(60)).append("\n\n");
        
        resultados.append("ESTADÍSTICAS GENERALES:\n");
        resultados.append("  • Archivos encontrados: ").append(cantidadArchivos).append("\n");
        resultados.append("  • Threads (hilos) trabajando: ").append(cantidadArchivos).append("\n");
        resultados.append("  • Tiempo total de procesamiento: ").append(tiempoTotal).append(" ms\n\n");
        resultados.append("═".repeat(60)).append("\n\n");
        
        resultados.append("DETALLE POR ARCHIVO:\n\n");
        
        int archivosExitosos = 0;
        for (int i = 0; i < contadores.size(); i++) {
            ContadorLineas contador = contadores.get(i);
            resultados.append(String.format("Archivo %d: %s\n", (i + 1), contador.getNombreArchivo()));
            
            if (contador.isExitoso()) {
                resultados.append(String.format("  ✓ Líneas contadas: %d\n", contador.getLineasContadas()));
                resultados.append(String.format("  ✓ Tiempo de ejecución: %d ms\n", contador.getTiempoEjecucion()));
                archivosExitosos++;
            } else {
                resultados.append(String.format("  ✗ Error: %s\n", contador.getError()));
            }
            resultados.append("\n");
        }
        
        resultados.append("═".repeat(60)).append("\n\n");
        
        resultados.append("RESULTADO FINAL:\n");
        resultados.append(String.format("  ★ TOTAL DE LÍNEAS: %d líneas\n", totalLineas));
        resultados.append(String.format("  ★ Archivos procesados exitosamente: %d de %d\n", 
                                       archivosExitosos, cantidadArchivos));
        
        if (archivosExitosos > 0) {
            double promedioLineas = (double) totalLineas / archivosExitosos;
            resultados.append(String.format("  ★ Promedio de líneas por archivo: %.2f\n", promedioLineas));
        }
        
        resultados.append("\n═".repeat(60));
        
        mostrarResultados(parent, "Resultados del Procesamiento", resultados.toString());
        
        String resumen = String.format(
            "PROCESAMIENTO COMPLETADO\n\n" +
            "Archivos encontrados: %d\n" +
            "Threads trabajando: %d\n" +
            "Tiempo total: %d ms\n\n" +
            "TOTAL DE LÍNEAS: %d",
            cantidadArchivos,
            cantidadArchivos,
            tiempoTotal,
            totalLineas
        );
        
        JOptionPane.showMessageDialog(
            parent,
            resumen,
            "Resumen",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}