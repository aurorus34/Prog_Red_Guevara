package ejercicio3;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class Main {
    
    // Variables compartidas para la carrera
    private static final int META = 70;
    private static final int SALIDA = 1;
    private static int posicionTortuga = SALIDA;
    private static int posicionLiebre = SALIDA;
    private static boolean carreraTerminada = false;
    private static String ganador = "";
    private static JTextArea areaTexto;
    private static final Object lock = new Object();
    

    static class Tortuga implements Runnable {
        private Random random = new Random();
        
        @Override
        public void run() {
            while (!carreraTerminada) {
                try {
                    Thread.sleep(1000); // Suspender por 1 segundo
                    
                    synchronized (lock) {
                        if (carreraTerminada) break;
                        
                        int probabilidad = random.nextInt(100) + 1; // 1 a 100
                        int movimiento = 0;
                        String accion = "";
                        
                        if (probabilidad <= 50) {
                            // Avance rápido - 50%
                            movimiento = 3;
                            accion = "Avance rápido (+3)";
                        } else if (probabilidad <= 70) {
                            // Resbaló - 20%
                            movimiento = -6;
                            accion = "Resbaló (-6)";
                        } else {
                            // Avance lento - 30%
                            movimiento = 1;
                            accion = "Avance lento (+1)";
                        }
                        
                        // Calcular nueva posición
                        posicionTortuga += movimiento;
                        
                        // No puede estar por debajo de la salida
                        if (posicionTortuga < SALIDA) {
                            posicionTortuga = SALIDA;
                        }
                        
                        // No puede superar la meta
                        if (posicionTortuga > META) {
                            posicionTortuga = META;
                        }
                        
                        appendTexto("TORTUGA: " + accion + " -> Posición: " + posicionTortuga + "\n");
                        
                        // Verificar si llegó a la meta
                        if (posicionTortuga >= META && !carreraTerminada) {
                            if (posicionLiebre >= META) {
                                ganador = "EMPATE";
                            } else {
                                ganador = "TORTUGA";
                            }
                            carreraTerminada = true;
                        }
                    }
                    
                } catch (InterruptedException e) {
                    appendTexto("Tortuga interrumpida\n");
                    break;
                }
            }
        }
    }
    

    static class Liebre implements Runnable {
        private Random random = new Random();
        
        @Override
        public void run() {
            while (!carreraTerminada) {
                try {
                    Thread.sleep(1000); // Suspender por 1 segundo
                    
                    synchronized (lock) {
                        if (carreraTerminada) break;
                        
                        int probabilidad = random.nextInt(100) + 1; // 1 a 100
                        int movimiento = 0;
                        String accion = "";
                        
                        if (probabilidad <= 20) {
                            // Duerme - 20%
                            movimiento = 0;
                            accion = "Duerme (0)";
                        } else if (probabilidad <= 40) {
                            // Gran salto - 20%
                            movimiento = 9;
                            accion = "Gran salto (+9)";
                        } else if (probabilidad <= 50) {
                            // Resbalón grande - 10%
                            movimiento = -12;
                            accion = "Resbalón grande (-12)";
                        } else if (probabilidad <= 80) {
                            // Pequeño salto - 30%
                            movimiento = 1;
                            accion = "Pequeño salto (+1)";
                        } else {
                            // Resbalón pequeño - 20%
                            movimiento = -2;
                            accion = "Resbalón pequeño (-2)";
                        }
                        
                        // Calcular nueva posición
                        posicionLiebre += movimiento;
                        
                        // No puede estar por debajo de la salida
                        if (posicionLiebre < SALIDA) {
                            posicionLiebre = SALIDA;
                        }
                        
                        // No puede superar la meta
                        if (posicionLiebre > META) {
                            posicionLiebre = META;
                        }
                        
                        appendTexto("LIEBRE:  " + accion + " -> Posición: " + posicionLiebre + "\n");
                        
                        // Verificar si llegó a la meta
                        if (posicionLiebre >= META && !carreraTerminada) {
                            if (posicionTortuga >= META) {
                                ganador = "EMPATE";
                            } else {
                                ganador = "LIEBRE";
                            }
                            carreraTerminada = true;
                        }
                        
                        // Mostrar posiciones visuales
                        mostrarPosiciones();
                    }
                    
                } catch (InterruptedException e) {
                    appendTexto("Liebre interrumpida\n");
                    break;
                }
            }
        }
    }
    
 
    private static void mostrarPosiciones() {
        // Crear línea para la tortuga
        StringBuilder lineaTortuga = new StringBuilder();
        for (int i = 1; i < posicionTortuga; i++) {
            lineaTortuga.append(" ");
        }
        lineaTortuga.append("T");
        
        // Crear línea para la liebre
        StringBuilder lineaLiebre = new StringBuilder();
        for (int i = 1; i < posicionLiebre; i++) {
            lineaLiebre.append(" ");
        }
        lineaLiebre.append("L");
        
        appendTexto(lineaTortuga.toString() + "\n");
        appendTexto(lineaLiebre.toString() + "\n");
        appendTexto("─".repeat(70) + "\n\n");
    }
    

    private static void appendTexto(String texto) {
        SwingUtilities.invokeLater(() -> {
            areaTexto.append(texto);
            areaTexto.setCaretPosition(areaTexto.getDocument().getLength());
        });
    }
    
    public static void main(String[] args) {
        // Crear ventana principal
        JFrame frame = new JFrame("Carrera: Tortuga vs Liebre");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        
        // Crear JTextArea con scroll
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        
        // Crear panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnIniciar = new JButton("Iniciar Carrera");
        JButton btnReiniciar = new JButton("Nueva Carrera");
        JButton btnSalir = new JButton("Salir");
        
        btnReiniciar.setEnabled(false);
        
        panelBotones.add(btnIniciar);
        panelBotones.add(btnReiniciar);
        panelBotones.add(btnSalir);
        
        // Layout
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);
        
        // Acción del botón Iniciar
        btnIniciar.addActionListener(e -> {
            btnIniciar.setEnabled(false);
            btnReiniciar.setEnabled(false);
            
            // Reiniciar variables
            posicionTortuga = SALIDA;
            posicionLiebre = SALIDA;
            carreraTerminada = false;
            ganador = "";
            areaTexto.setText("");
            
            // Mensaje de inicio
            appendTexto("═".repeat(70) + "\n");
            appendTexto("        ¡COMIENZA LA CARRERA ENTRE LA TORTUGA Y LA LIEBRE!\n");
            appendTexto("═".repeat(70) + "\n");
            appendTexto("Meta: 70 casillas | Salida: 1\n");
            appendTexto("═".repeat(70) + "\n\n");
            
            // Crear y ejecutar threads
            Tortuga tortuga = new Tortuga();
            Liebre liebre = new Liebre();
            
            Thread threadTortuga = new Thread(tortuga);
            Thread threadLiebre = new Thread(liebre);
            
            threadTortuga.start();
            threadLiebre.start();
            
            // Thread para detectar el final
            new Thread(() -> {
                try {
                    threadTortuga.join();
                    threadLiebre.join();
                    
                    // Mostrar resultado final
                    SwingUtilities.invokeLater(() -> {
                        appendTexto("\n");
                        appendTexto("═".repeat(70) + "\n");
                        appendTexto("                    ¡CARRERA TERMINADA!\n");
                        appendTexto("═".repeat(70) + "\n\n");
                        
                        if (ganador.equals("EMPATE")) {
                            appendTexto("                      ¡ES UN EMPATE!\n");
                            appendTexto("           ¡Ambos llegaron a la meta al mismo tiempo!\n");
                        } else if (ganador.equals("TORTUGA")) {
                            appendTexto("                  ¡GANA LA TORTUGA!\n");
                            appendTexto("              ¡Lento pero seguro gana la carrera!\n");
                        } else if (ganador.equals("LIEBRE")) {
                            appendTexto("                   ¡GANA LA LIEBRE!\n");
                            appendTexto("               ¡La velocidad es la clave del éxito!\n");
                        }
                        
                        appendTexto("\nPosición final - Tortuga: " + posicionTortuga);
                        appendTexto(" | Liebre: " + posicionLiebre + "\n");
                        appendTexto("═".repeat(70) + "\n");
                        
                        btnReiniciar.setEnabled(true);
                        
                        // Mostrar diálogo con el ganador
                        String mensaje = ganador.equals("EMPATE") ? 
                            "¡ES UN EMPATE!\n\nAmbos llegaron a la meta al mismo tiempo." :
                            "¡GANA LA " + ganador + "!\n\nPosición final:\nTortuga: " + 
                            posicionTortuga + "\nLiebre: " + posicionLiebre;
                        
                        JOptionPane.showMessageDialog(
                            frame,
                            mensaje,
                            "Resultado de la Carrera",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    });
                    
                } catch (InterruptedException ex) {
                    appendTexto("Error al esperar los threads\n");
                }
            }).start();
        });
        
        // Acción del botón Reiniciar
        btnReiniciar.addActionListener(e -> {
            btnIniciar.setEnabled(true);
            btnReiniciar.setEnabled(false);
        });
        
        // Acción del botón Salir
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
        
        // Mostrar ventana
        frame.setVisible(true);
        
        // Mostrar instrucciones iniciales
        JOptionPane.showMessageDialog(
            frame,
            "Bienvenido a la carrera entre la Tortuga y la Liebre\n\n" +
            "Reglas:\n" +
            "- La carrera es de 70 casillas\n" +
            "- Cada segundo los animales se mueven según probabilidades\n" +
            "- Pueden avanzar o retroceder\n" +
            "- El primero en llegar a la casilla 70 gana\n\n" +
            "Presione 'Iniciar Carrera' para comenzar",
            "Instrucciones",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}