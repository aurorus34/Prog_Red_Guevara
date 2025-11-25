package ejercicio6;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class Main {
    
    private static ArrayList<Alumno> listaAlumnos = new ArrayList<>();
    private static final Object lock = new Object();
    

    static class Alumno {
        private String nombre;
        private String apellido;
        private boolean[] asistencia; // 9 días de asistencia
        private double[] notas; // 3 notas
        private boolean esAlumnoRegular;
        private double porcentajePresentismo;
        private double promedio;
        
        public Alumno(String nombre, String apellido) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.asistencia = new boolean[9];
            this.notas = new double[3];
            this.esAlumnoRegular = true; 
            this.porcentajePresentismo = 0;
            this.promedio = 0;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public String getApellido() {
            return apellido;
        }
        
        public String getNombreCompleto() {
            return apellido + ", " + nombre;
        }
        
        public boolean[] getAsistencia() {
            return asistencia;
        }
        
        public void setAsistencia(boolean[] asistencia) {
            this.asistencia = asistencia;
        }
        
        public double[] getNotas() {
            return notas;
        }
        
        public void setNotas(double[] notas) {
            this.notas = notas;
        }
        
        public boolean isEsAlumnoRegular() {
            return esAlumnoRegular;
        }
        
        public void setEsAlumnoRegular(boolean esAlumnoRegular) {
            this.esAlumnoRegular = esAlumnoRegular;
        }
        
        public double getPorcentajePresentismo() {
            return porcentajePresentismo;
        }
        
        public void setPorcentajePresentismo(double porcentajePresentismo) {
            this.porcentajePresentismo = porcentajePresentismo;
        }
        
        public double getPromedio() {
            return promedio;
        }
        
        public void setPromedio(double promedio) {
            this.promedio = promedio;
        }
        
        @Override
        public String toString() {
            return String.format("%s %s - Presentismo: %.1f%% - Promedio: %.2f - %s",
                nombre, apellido, porcentajePresentismo, promedio,
                esAlumnoRegular ? "REGULAR" : "LIBRE");
        }
    }
    

    static class OficinaDeAlumnos implements Runnable {
        @Override
        public void run() {
            System.out.println("\n>>> OFICINA DE ALUMNOS: Iniciando carga de alumnos...");
            
            synchronized (lock) {
                listaAlumnos.add(new Alumno("Juan", "Pérez"));
                listaAlumnos.add(new Alumno("María", "González"));
                listaAlumnos.add(new Alumno("Carlos", "Rodríguez"));
                listaAlumnos.add(new Alumno("Ana", "Martínez"));
                listaAlumnos.add(new Alumno("Pedro", "López"));
                listaAlumnos.add(new Alumno("Laura", "Fernández"));
                
                System.out.println("✓ OFICINA DE ALUMNOS: Se cargaron " + listaAlumnos.size() + " alumnos");
                
                for (Alumno alumno : listaAlumnos) {
                    System.out.println("  - " + alumno.getNombreCompleto());
                }
            }
            
            System.out.println("✓ OFICINA DE ALUMNOS: Proceso completado\n");
        }
    }
    

    static class Preceptor implements Runnable {
        private Random random = new Random();
        
        @Override
        public void run() {
            System.out.println(">>> PRECEPTOR: Iniciando registro de asistencias...");
            
            try {
                Thread.sleep(500); 
                
                synchronized (lock) {
                    for (Alumno alumno : listaAlumnos) {
                        boolean[] asistencia = new boolean[9];
                        int diasPresentes = 0;
                        
                        for (int i = 0; i < 9; i++) {
                            asistencia[i] = random.nextBoolean();
                            if (asistencia[i]) {
                                diasPresentes++;
                            }
                        }
                        
                        alumno.setAsistencia(asistencia);
                        
                        double porcentaje = (diasPresentes * 100.0) / 9.0;
                        alumno.setPorcentajePresentismo(porcentaje);
                        
                        System.out.println("  PRECEPTOR: " + alumno.getNombreCompleto() + 
                                         " - Presentismo: " + String.format("%.1f%%", porcentaje) +
                                         " (" + diasPresentes + "/9 días)");
                    }
                }
                
                System.out.println("✓ PRECEPTOR: Registro de asistencias completado\n");
                
            } catch (InterruptedException e) {
                System.err.println("✗ PRECEPTOR: Proceso interrumpido");
            }
        }
        

        public void alumnoLibre() {
            System.out.println(">>> PRECEPTOR: Evaluando alumnos libres (< 75% asistencia)...");
            
            synchronized (lock) {
                for (Alumno alumno : listaAlumnos) {
                    if (alumno.getPorcentajePresentismo() < 75.0) {
                        alumno.setEsAlumnoRegular(false);
                        System.out.println("  ✗ " + alumno.getNombreCompleto() + 
                                         " pasa a condición de LIBRE (Presentismo: " + 
                                         String.format("%.1f%%", alumno.getPorcentajePresentismo()) + ")");
                    }
                }
            }
            
            System.out.println("✓ PRECEPTOR: Evaluación completada\n");
        }
    }
    

    static class Docente implements Runnable {
        private Random random = new Random();
        
        @Override
        public void run() {
            System.out.println(">>> DOCENTE: Iniciando ingreso de notas...");
            
            try {
                Thread.sleep(500); 
                
                synchronized (lock) {
                    for (Alumno alumno : listaAlumnos) {
                        double[] notas = new double[3];
                        
                        for (int i = 0; i < 3; i++) {
                            notas[i] = 1 + (random.nextDouble() * 9); 
                        }
                        
                        alumno.setNotas(notas);
                        
                        System.out.println("  DOCENTE: " + alumno.getNombreCompleto() + 
                                         " - Notas: " + String.format("%.2f, %.2f, %.2f", 
                                         notas[0], notas[1], notas[2]));
                    }
                }
                
                System.out.println("✓ DOCENTE: Ingreso de notas completado\n");
                
            } catch (InterruptedException e) {
                System.err.println("✗ DOCENTE: Proceso interrumpido");
            }
        }
        

        public void promedio() {
            System.out.println(">>> DOCENTE: Calculando promedios...");
            
            synchronized (lock) {
                for (Alumno alumno : listaAlumnos) {
                    double suma = 0;
                    for (double nota : alumno.getNotas()) {
                        suma += nota;
                    }
                    double promedio = suma / alumno.getNotas().length;
                    alumno.setPromedio(promedio);
                    
                    System.out.println("  DOCENTE: " + alumno.getNombreCompleto() + 
                                     " - Promedio: " + String.format("%.2f", promedio));
                }
            }
            
            System.out.println("✓ DOCENTE: Cálculo de promedios completado\n");
        }
    }
    

    private static String generarReporteCompleto() {
        StringBuilder reporte = new StringBuilder();
        
        reporte.append("═══════════════════════════════════════════════════════════════\n");
        reporte.append("               SISTEMA DE GESTIÓN DE ALUMNOS\n");
        reporte.append("═══════════════════════════════════════════════════════════════\n\n");
        
        reporte.append("TOTAL DE ALUMNOS: ").append(listaAlumnos.size()).append("\n\n");
        reporte.append("═".repeat(63)).append("\n\n");
        
        reporte.append("LISTADO COMPLETO DE ALUMNOS:\n\n");
        
        int contador = 1;
        for (Alumno alumno : listaAlumnos) {
            reporte.append(contador++).append(". ").append(alumno.getNombreCompleto()).append("\n");
            reporte.append("   Presentismo: ").append(String.format("%.1f%%", alumno.getPorcentajePresentismo()));
            
            reporte.append(" [");
            for (int i = 0; i < alumno.getAsistencia().length; i++) {
                reporte.append(alumno.getAsistencia()[i] ? "P" : "A");
                if (i < alumno.getAsistencia().length - 1) reporte.append(" ");
            }
            reporte.append("]\n");
            
            reporte.append("   Notas: ");
            for (int i = 0; i < alumno.getNotas().length; i++) {
                reporte.append(String.format("%.2f", alumno.getNotas()[i]));
                if (i < alumno.getNotas().length - 1) reporte.append(", ");
            }
            reporte.append("\n");
            
            reporte.append("   Promedio: ").append(String.format("%.2f", alumno.getPromedio())).append("\n");
            reporte.append("   Estado: ").append(alumno.isEsAlumnoRegular() ? "REGULAR" : "LIBRE").append("\n\n");
        }
        
        reporte.append("═".repeat(63)).append("\n\n");
        
        reporte.append("ALUMNOS EXIMIDOS (Regulares con promedio ≥ 7.00):\n\n");
        
        ArrayList<Alumno> eximidos = new ArrayList<>();
        for (Alumno alumno : listaAlumnos) {
            if (alumno.isEsAlumnoRegular() && alumno.getPromedio() >= 7.0) {
                eximidos.add(alumno);
            }
        }
        
        if (eximidos.isEmpty()) {
            reporte.append("   No hay alumnos eximidos.\n\n");
        } else {
            for (Alumno alumno : eximidos) {
                reporte.append("   ★ ").append(alumno.getNombreCompleto());
                reporte.append(" - Nota Final: ").append(String.format("%.2f", alumno.getPromedio()));
                reporte.append(" - Presentismo: ").append(String.format("%.1f%%", alumno.getPorcentajePresentismo()));
                reporte.append("\n");
            }
            reporte.append("\n   Total de eximidos: ").append(eximidos.size()).append("\n\n");
        }
        
        reporte.append("═".repeat(63)).append("\n\n");
        
        reporte.append("ALUMNOS REGULARES (Deben rendir examen):\n\n");
        
        ArrayList<Alumno> regulares = new ArrayList<>();
        for (Alumno alumno : listaAlumnos) {
            if (alumno.isEsAlumnoRegular() && alumno.getPromedio() < 7.0) {
                regulares.add(alumno);
            }
        }
        
        if (regulares.isEmpty()) {
            reporte.append("   No hay alumnos regulares que deban rendir.\n\n");
        } else {
            for (Alumno alumno : regulares) {
                reporte.append("   • ").append(alumno.getNombreCompleto());
                reporte.append(" - Promedio: ").append(String.format("%.2f", alumno.getPromedio()));
                reporte.append(" - Presentismo: ").append(String.format("%.1f%%", alumno.getPorcentajePresentismo()));
                reporte.append("\n");
            }
            reporte.append("\n   Total de regulares: ").append(regulares.size()).append("\n\n");
        }
        
        reporte.append("═".repeat(63)).append("\n\n");
        
        reporte.append("ALUMNOS LIBRES (Presentismo < 75%):\n\n");
        
        ArrayList<Alumno> libres = new ArrayList<>();
        for (Alumno alumno : listaAlumnos) {
            if (!alumno.isEsAlumnoRegular()) {
                libres.add(alumno);
            }
        }
        
        if (libres.isEmpty()) {
            reporte.append("   No hay alumnos en condición de libre.\n\n");
        } else {
            for (Alumno alumno : libres) {
                reporte.append("   ✗ ").append(alumno.getNombreCompleto());
                reporte.append(" - Promedio: ").append(String.format("%.2f", alumno.getPromedio()));
                reporte.append(" - Presentismo: ").append(String.format("%.1f%%", alumno.getPorcentajePresentismo()));
                reporte.append("\n");
            }
            reporte.append("\n   Total de libres: ").append(libres.size()).append("\n\n");
        }
        
        reporte.append("═".repeat(63)).append("\n\n");
        
        double promedioGeneral = 0;
        double presentismoGeneral = 0;
        for (Alumno alumno : listaAlumnos) {
            promedioGeneral += alumno.getPromedio();
            presentismoGeneral += alumno.getPorcentajePresentismo();
        }
        promedioGeneral /= listaAlumnos.size();
        presentismoGeneral /= listaAlumnos.size();
        
        reporte.append("ESTADÍSTICAS GENERALES:\n\n");
        reporte.append("   Promedio general del curso: ").append(String.format("%.2f", promedioGeneral)).append("\n");
        reporte.append("   Presentismo general: ").append(String.format("%.1f%%", presentismoGeneral)).append("\n");
        reporte.append("   Alumnos eximidos: ").append(eximidos.size()).append("\n");
        reporte.append("   Alumnos regulares: ").append(regulares.size()).append("\n");
        reporte.append("   Alumnos libres: ").append(libres.size()).append("\n");
        
        reporte.append("\n═".repeat(63));
        
        return reporte.toString();
    }
    

    private static void mostrarResultados(JFrame parent, String titulo, String contenido) {
        JDialog dialog = new JDialog(parent, titulo, true);
        dialog.setSize(750, 600);
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
            JFrame frame = new JFrame("Sistema de Gestión de Alumnos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel lblTitulo = new JLabel("Sistema de Gestión de Alumnos", JLabel.CENTER);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
            
            JButton btnIniciar = new JButton("Iniciar Proceso Completo");
            JButton btnVerReporte = new JButton("Ver Reporte Completo");
            JButton btnAyuda = new JButton("Ayuda");
            JButton btnSalir = new JButton("Salir");
            
            btnVerReporte.setEnabled(false);
            
            panel.add(lblTitulo);
            panel.add(btnIniciar);
            panel.add(btnVerReporte);
            panel.add(btnAyuda);
            panel.add(btnSalir);
            
            frame.add(panel);
            
            btnIniciar.addActionListener(e -> {
                btnIniciar.setEnabled(false);
                btnVerReporte.setEnabled(false);
                
                listaAlumnos.clear();
                
                System.out.println("\n" + "═".repeat(70));
                System.out.println("           INICIANDO SISTEMA DE GESTIÓN DE ALUMNOS");
                System.out.println("═".repeat(70));
                
                OficinaDeAlumnos oficina = new OficinaDeAlumnos();
                Thread threadOficina = new Thread(oficina);
                threadOficina.start();
                
                try {
                    threadOficina.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                
                Preceptor preceptor = new Preceptor();
                Docente docente = new Docente();
                
                Thread threadPreceptor = new Thread(preceptor);
                Thread threadDocente = new Thread(docente);
                
                threadPreceptor.start();
                threadDocente.start();
                
                try {
                    threadPreceptor.join();
                    threadDocente.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                
                System.out.println("═".repeat(70));
                System.out.println("     PRECEPTOR Y DOCENTE HAN TERMINADO - PROCESANDO DATOS");
                System.out.println("═".repeat(70) + "\n");
                
                docente.promedio(); 
                preceptor.alumnoLibre();
                
                System.out.println("═".repeat(70));
                System.out.println("              PROCESO COMPLETADO - GENERANDO REPORTE");
                System.out.println("═".repeat(70) + "\n");
                
                String reporte = generarReporteCompleto();
                
                SwingUtilities.invokeLater(() -> {
                    mostrarResultados(frame, "Reporte del Sistema", reporte);
                    btnIniciar.setEnabled(true);
                    btnVerReporte.setEnabled(true);
                    
                    StringBuilder resumenEximidos = new StringBuilder();
                    resumenEximidos.append("ALUMNOS EXIMIDOS:\n");
                    resumenEximidos.append("(Regulares con promedio ≥ 7.00)\n\n");
                    
                    boolean hayEximidos = false;
                    for (Alumno alumno : listaAlumnos) {
                        if (alumno.isEsAlumnoRegular() && alumno.getPromedio() >= 7.0) {
                            resumenEximidos.append("★ ").append(alumno.getNombreCompleto());
                            resumenEximidos.append("\n   Nota Final: ").append(String.format("%.2f", alumno.getPromedio()));
                            resumenEximidos.append("\n   Presentismo: ").append(String.format("%.1f%%\n\n", alumno.getPorcentajePresentismo()));
                            hayEximidos = true;
                        }
                    }
                    
                    if (!hayEximidos) {
                        resumenEximidos.append("No hay alumnos eximidos.");
                    }
                    
                    JOptionPane.showMessageDialog(
                        frame,
                        resumenEximidos.toString(),
                        "Alumnos Eximidos",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                });
            });
            
            btnVerReporte.addActionListener(e -> {
                if (listaAlumnos.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        frame,
                        "No hay datos disponibles.\nDebe iniciar el proceso primero.",
                        "Sin Datos",
                        JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    String reporte = generarReporteCompleto();
                    mostrarResultados(frame, "Reporte Completo del Sistema", reporte);
                }
            });
            
            btnAyuda.addActionListener(e -> {
                String ayuda = "═══════════ SISTEMA DE GESTIÓN DE ALUMNOS ═══════════\n\n" +
                    "DESCRIPCIÓN:\n" +
                    "Este sistema gestiona alumnos usando 3 threads:\n\n" +
                    "1. OFICINA DE ALUMNOS:\n" +
                    "   • Carga el listado inicial de alumnos\n\n" +
                    "2. PRECEPTOR:\n" +
                    "   • Registra asistencias (9 días)\n" +
                    "   • Calcula presentismo\n" +
                    "   • Marca como LIBRE a alumnos con < 75% asistencia\n\n" +
                    "3. DOCENTE:\n" +
                    "   • Ingresa 3 notas por alumno\n" +
                    "   • Calcula promedio de notas\n\n" +
                    "CONDICIONES:\n" +
                    "• EXIMIDO: Regular + Promedio ≥ 7.00\n" +
                    "• REGULAR: Presentismo ≥ 75% + Promedio < 7.00\n" +
                    "• LIBRE: Presentismo < 75%\n\n" +
                    "PROCESO:\n" +
                    "1. Se ejecuta OficinaDeAlumnos primero\n" +
                    "2. Luego se ejecutan Preceptor y Docente simultáneamente\n" +
                    "3. El main espera que terminen ambos\n" +
                    "4. Se calculan promedios y se marcan alumnos libres\n" +
                    "5. Se genera el reporte final con alumnos eximidos\n\n" +
                    "DATOS GENERADOS:\n" +
                    "• Asistencias: Aleatorias (verdadero/falso)\n" +
                    "• Notas: Aleatorias entre 1.00 y 10.00";
                
                mostrarResultados(frame, "Ayuda del Sistema", ayuda);
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
                "Bienvenido al Sistema de Gestión de Alumnos\n\n" +
                "Este sistema procesa información de alumnos usando threads:\n" +
                "• OficinaDeAlumnos: Carga alumnos\n" +
                "• Preceptor: Registra asistencias\n" +
                "• Docente: Ingresa notas\n\n" +
                "Presione 'Iniciar Proceso Completo' para comenzar.\n" +
                "El sistema generará datos aleatorios y mostrará\n" +
                "la lista de alumnos eximidos al finalizar.",
                "Bienvenida",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}