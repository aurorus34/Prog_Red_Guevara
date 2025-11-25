package ejercicio7;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class Main {
    
    private static Empleado[] arrayPersonal;
    private static int cantidadEmpleados;
    private static int empleadosCargados = 0;
    private static final Object lock = new Object();
    private static volatile boolean cargaCompleta = false;
    private static final LocalTime HORA_ENTRADA = LocalTime.of(8, 0); // 08:00
    private static JTextArea areaResultados;
    

    static class Empleado {
        private String nombre;
        private String dia;
        private LocalTime horaIngreso;
        private boolean llegaTemprano;
        private boolean procesado;
        
        public Empleado() {
            this.procesado = false;
        }
        
        public String getNombre() {
            return nombre;
        }
        
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        
        public String getDia() {
            return dia;
        }
        
        public void setDia(String dia) {
            this.dia = dia;
        }
        
        public LocalTime getHoraIngreso() {
            return horaIngreso;
        }
        
        public void setHoraIngreso(LocalTime horaIngreso) {
            this.horaIngreso = horaIngreso;
        }
        
        public boolean isLlegaTemprano() {
            return llegaTemprano;
        }
        
        public void setLlegaTemprano(boolean llegaTemprano) {
            this.llegaTemprano = llegaTemprano;
        }
        
        public boolean isProcesado() {
            return procesado;
        }
        
        public void setProcesado(boolean procesado) {
            this.procesado = procesado;
        }
    }
    

    static class RevisorPersonal implements Runnable {
        @Override
        public void run() {
            appendTexto("\n>>> REVISOR DE PERSONAL: Hilo iniciado y monitoreando...\n\n");
            
            int ultimoRevisado = 0;
            
            while (!cargaCompleta || ultimoRevisado < empleadosCargados) {
                synchronized (lock) {
                    for (int i = ultimoRevisado; i < empleadosCargados; i++) {
                        if (arrayPersonal[i] != null && !arrayPersonal[i].isProcesado()) {
                            procesarEmpleado(arrayPersonal[i], i + 1);
                            arrayPersonal[i].setProcesado(true);
                            ultimoRevisado = i + 1;
                        }
                    }
                }
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    appendTexto("✗ REVISOR: Proceso interrumpido\n");
                    break;
                }
            }
            
            appendTexto("\n>>> REVISOR DE PERSONAL: Todos los empleados han sido procesados\n");
            appendTexto("═".repeat(70) + "\n");
            appendTexto("              CONTROL DE ASISTENCIA FINALIZADO\n");
            appendTexto("═".repeat(70) + "\n\n");
            
            generarResumenFinal();
        }
        

        private void procesarEmpleado(Empleado empleado, int numero) {
            LocalTime horaIngreso = empleado.getHoraIngreso();
            
            // Determinar si llegó temprano o tarde
            boolean llegaTemprano = horaIngreso.isBefore(HORA_ENTRADA) || 
                                   horaIngreso.equals(HORA_ENTRADA);
            empleado.setLlegaTemprano(llegaTemprano);
            
            int minutosDiferencia = Math.abs(
                horaIngreso.toSecondOfDay() - HORA_ENTRADA.toSecondOfDay()
            ) / 60;
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("─".repeat(70)).append("\n");
            mensaje.append("EMPLEADO #").append(numero).append(": ").append(empleado.getNombre()).append("\n");
            mensaje.append("  Día: ").append(empleado.getDia()).append("\n");
            mensaje.append("  Hora de ingreso: ").append(horaIngreso.format(formatter)).append("\n");
            mensaje.append("  Hora esperada: 08:00\n");
            
            if (llegaTemprano) {
                if (minutosDiferencia == 0) {
                    mensaje.append("  ✓ Estado: LLEGÓ PUNTUAL\n");
                } else {
                    mensaje.append("  ✓ Estado: LLEGÓ TEMPRANO (").append(minutosDiferencia)
                           .append(" minutos antes)\n");
                }
            } else {
                mensaje.append("  ✗ Estado: LLEGÓ TARDE (").append(minutosDiferencia)
                       .append(" minutos de retraso)\n");
            }
            
            mensaje.append("─".repeat(70)).append("\n\n");
            
            appendTexto(mensaje.toString());
        }
        

        private void generarResumenFinal() {
            int temprano = 0;
            int tarde = 0;
            
            for (int i = 0; i < empleadosCargados; i++) {
                if (arrayPersonal[i].isLlegaTemprano()) {
                    temprano++;
                } else {
                    tarde++;
                }
            }
            
            StringBuilder resumen = new StringBuilder();
            resumen.append("RESUMEN FINAL DE ASISTENCIAS:\n\n");
            resumen.append("  Total de empleados: ").append(empleadosCargados).append("\n");
            resumen.append("  Llegaron temprano/puntual: ").append(temprano).append("\n");
            resumen.append("  Llegaron tarde: ").append(tarde).append("\n\n");
            
            if (tarde == 0) {
                resumen.append("  ★ ¡EXCELENTE! Todos los empleados llegaron a tiempo.\n");
            } else {
                double porcentajeTarde = (tarde * 100.0) / empleadosCargados;
                resumen.append("  Porcentaje de tardanzas: ").append(String.format("%.1f%%", porcentajeTarde)).append("\n");
            }
            
            resumen.append("\n═".repeat(70) + "\n");
            resumen.append("El programa se cerrará automáticamente en 10 segundos...\n");
            resumen.append("═".repeat(70) + "\n");
            
            appendTexto(resumen.toString());
            
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    

    private static void appendTexto(String texto) {
        SwingUtilities.invokeLater(() -> {
            areaResultados.append(texto);
            areaResultados.setCaretPosition(areaResultados.getDocument().getLength());
        });
        System.out.print(texto);
    }
    

    private static LocalTime validarHora(String horaStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(horaStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Control de Asistencia de Empleados");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 650);
            frame.setLocationRelativeTo(null);
            
            areaResultados = new JTextArea();
            areaResultados.setEditable(false);
            areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
            areaResultados.setMargin(new Insets(10, 10, 10, 10));
            JScrollPane scrollPane = new JScrollPane(areaResultados);
            
            frame.add(scrollPane);
            frame.setVisible(true);
            
            appendTexto("═".repeat(70) + "\n");
            appendTexto("         SISTEMA DE CONTROL DE ASISTENCIA DE EMPLEADOS\n");
            appendTexto("═".repeat(70) + "\n\n");
            appendTexto("Hora de entrada establecida: 08:00\n\n");
            
            while (true) {
                String input = JOptionPane.showInputDialog(
                    frame,
                    "Ingrese la cantidad total de empleados:",
                    "Cantidad de Empleados",
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (input == null) {
                    int confirmacion = JOptionPane.showConfirmDialog(
                        frame,
                        "¿Desea salir del programa?",
                        "Confirmar Salida",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirmacion == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                    continue;
                }
                
                if (input.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                        frame,
                        "Error: Debe ingresar un valor.",
                        "Entrada Inválida",
                        JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }
                
                try {
                    cantidadEmpleados = Integer.parseInt(input.trim());
                    if (cantidadEmpleados <= 0) {
                        JOptionPane.showMessageDialog(
                            frame,
                            "Error: La cantidad debe ser mayor a 0.",
                            "Valor Inválido",
                            JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                        frame,
                        "Error: Debe ingresar un número entero válido.",
                        "Entrada Inválida",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            
            arrayPersonal = new Empleado[cantidadEmpleados];
            
            appendTexto("Cantidad de empleados a registrar: " + cantidadEmpleados + "\n\n");
            appendTexto("═".repeat(70) + "\n\n");
            
            RevisorPersonal revisor = new RevisorPersonal();
            Thread threadRevisor = new Thread(revisor);
            threadRevisor.start();
            
            new Thread(() -> {
                for (int i = 0; i < cantidadEmpleados; i++) {
                    final int indice = i;
                    Empleado empleado = new Empleado();
                    
                    SwingUtilities.invokeLater(() -> {
                        appendTexto("Cargando empleado #" + (indice + 1) + "...\n");
                    });
                    
                    String nombre = null;
                    while (true) {
                        nombre = JOptionPane.showInputDialog(
                            frame,
                            "Empleado #" + (indice + 1) + " de " + cantidadEmpleados + 
                            "\n\nIngrese el nombre del empleado:",
                            "Nombre del Empleado",
                            JOptionPane.QUESTION_MESSAGE
                        );
                        
                        if (nombre == null) {
                            int confirmacion = JOptionPane.showConfirmDialog(
                                frame,
                                "¿Desea cancelar? El programa se cerrará.",
                                "Confirmar Cancelación",
                                JOptionPane.YES_NO_OPTION
                            );
                            if (confirmacion == JOptionPane.YES_OPTION) {
                                System.exit(0);
                            }
                            continue;
                        }
                        
                        if (nombre.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(
                                frame,
                                "Error: El nombre no puede estar vacío.",
                                "Entrada Inválida",
                                JOptionPane.ERROR_MESSAGE
                            );
                            continue;
                        }
                        
                        break;
                    }
                    empleado.setNombre(nombre.trim());
                    
                    String dia = null;
                    while (true) {
                        dia = JOptionPane.showInputDialog(
                            frame,
                            "Empleado: " + empleado.getNombre() + 
                            "\n\nIngrese el día de ingreso (ej: Lunes, Martes, etc.):",
                            "Día de Ingreso",
                            JOptionPane.QUESTION_MESSAGE
                        );
                        
                        if (dia == null) {
                            int confirmacion = JOptionPane.showConfirmDialog(
                                frame,
                                "¿Desea cancelar? El programa se cerrará.",
                                "Confirmar Cancelación",
                                JOptionPane.YES_NO_OPTION
                            );
                            if (confirmacion == JOptionPane.YES_OPTION) {
                                System.exit(0);
                            }
                            continue;
                        }
                        
                        if (dia.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(
                                frame,
                                "Error: El día no puede estar vacío.",
                                "Entrada Inválida",
                                JOptionPane.ERROR_MESSAGE
                            );
                            continue;
                        }
                        
                        break;
                    }
                    empleado.setDia(dia.trim());
                    
                    LocalTime hora = null;
                    while (true) {
                        String horaStr = JOptionPane.showInputDialog(
                            frame,
                            "Empleado: " + empleado.getNombre() + 
                            "\nDía: " + empleado.getDia() +
                            "\n\nIngrese la hora de ingreso (formato HH:mm, ej: 08:30):\n" +
                            "Hora de entrada establecida: 08:00",
                            "Hora de Ingreso",
                            JOptionPane.QUESTION_MESSAGE
                        );
                        
                        if (horaStr == null) {
                            int confirmacion = JOptionPane.showConfirmDialog(
                                frame,
                                "¿Desea cancelar? El programa se cerrará.",
                                "Confirmar Cancelación",
                                JOptionPane.YES_NO_OPTION
                            );
                            if (confirmacion == JOptionPane.YES_OPTION) {
                                System.exit(0);
                            }
                            continue;
                        }
                        
                        if (horaStr.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(
                                frame,
                                "Error: La hora no puede estar vacía.",
                                "Entrada Inválida",
                                JOptionPane.ERROR_MESSAGE
                            );
                            continue;
                        }
                        
                        hora = validarHora(horaStr.trim());
                        if (hora == null) {
                            JOptionPane.showMessageDialog(
                                frame,
                                "Error: Formato de hora inválido.\n" +
                                "Use el formato HH:mm (ej: 08:30, 07:45, 09:15)",
                                "Formato Inválido",
                                JOptionPane.ERROR_MESSAGE
                            );
                            continue;
                        }
                        
                        break;
                    }
                    empleado.setHoraIngreso(hora);
                    
                    synchronized (lock) {
                        arrayPersonal[indice] = empleado;
                        empleadosCargados++;
                    }
                    
                    SwingUtilities.invokeLater(() -> {
                        appendTexto("✓ Empleado #" + (indice + 1) + " registrado: " + 
                                   empleado.getNombre() + "\n\n");
                    });
                    
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
 
                cargaCompleta = true;
                
                SwingUtilities.invokeLater(() -> {
                    appendTexto("\n✓ CARGA COMPLETA: Todos los empleados han sido registrados\n");
                });
                
            }).start();
        });
    }
}