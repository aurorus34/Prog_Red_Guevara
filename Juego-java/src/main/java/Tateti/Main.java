package Tateti;

import java.io.*;
import java.net.*;

/**
 * Maneja la conexión con el servidor y el control general del juego
 */
public class Cliente {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private InterfazConsola interfaz;
    private String miSimbolo;
    private String miNombre;
    private Tablero tableroLocal;
    private Thread hiloReceptor;
    private boolean conectado;
    
    // Configuración de reintentos
    private static final int MAX_INTENTOS_CONEXION = 3;
    private static final int DELAY_REINTENTO_MS = 2000;
    
    public Cliente(String host, int puerto) throws IOException {
        this.interfaz = new InterfazConsola();
        this.tableroLocal = new Tablero();
        this.conectado = false;
        
        interfaz.mostrarBannerPrincipal();
        
        // Intentar conectar con reintentos
        boolean exito = conectarConReintentos(host, puerto);
        
        if (exito) {
            this.conectado = true;
            interfaz.mostrarConexionExitosa();
            InterfazConsola.debug("Cliente creado exitosamente");
        } else {
            throw new IOException("No se pudo conectar al servidor después de " + 
                                MAX_INTENTOS_CONEXION + " intentos");
        }
    }
    
    /**
     * Intenta conectar al servidor con múltiples reintentos
     */
    private boolean conectarConReintentos(String host, int puerto) {
        for (int intento = 1; intento <= MAX_INTENTOS_CONEXION; intento++) {
            try {
                InterfazConsola.debug("Intento de conexión #" + intento);
                socket = new Socket(host, puerto);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                InterfazConsola.debug("Conexión establecida");
                return true;
                
            } catch (IOException e) {
                InterfazConsola.debug("Intento #" + intento + " falló: " + e.getMessage());
                
                if (intento < MAX_INTENTOS_CONEXION) {
                    interfaz.mostrarReintento(intento, MAX_INTENTOS_CONEXION);
                    try {
                        Thread.sleep(DELAY_REINTENTO_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Establece la conexión con el servidor y configura el receptor
     */
    public void conectarConServidor() {
        try {
            // Recibir mensaje de bienvenida
            String mensaje = in.readLine();
            InterfazConsola.debug("Mensaje de bienvenida: " + mensaje);
            
            if (mensaje != null && mensaje.startsWith("BIENVENIDO:")) {
                String[] partes = mensaje.split(":");
                if (partes.length >= 3) {
                    miNombre = partes[1];
                    miSimbolo = partes[2];
                    
                    interfaz.mostrarBienvenida(miNombre, miSimbolo);
                    interfaz.mostrarInstrucciones();
                    
                    InterfazConsola.debug("Jugador configurado: " + miNombre + " (" + miSimbolo + ")");
                }
            }
            
            // Iniciar thread para recibir mensajes
            ReceptorMensajes receptor = new ReceptorMensajes(in, this, interfaz, 
                                                             tableroLocal, miNombre);
            hiloReceptor = new Thread(receptor);
            hiloReceptor.setName("ReceptorMensajes");
            hiloReceptor.start();
            
            InterfazConsola.debug("Hilo receptor iniciado");
            
        } catch (IOException e) {
            interfaz.mostrarErrorConexion("Error al establecer conexión: " + e.getMessage());
            cerrarConexion(true);
        }
    }
    
    /**
     * Permite al jugador realizar un movimiento
     */
    public void realizarMovimiento() {
        try {
            InterfazConsola.debug("Solicitando movimiento al jugador");
            
            interfaz.mostrarEncabezadoTurno(miNombre);
            tableroLocal.mostrarTablero();
            
            // Leer coordenadas con validación
            int[] coordenadas = interfaz.leerCoordenadas(tableroLocal);
            int fila = coordenadas[0];
            int col = coordenadas[1];
            
            // Enviar movimiento al servidor
            String mensaje = "MOVIMIENTO:" + fila + ":" + col;
            out.println(mensaje);
            InterfazConsola.debug("Movimiento enviado: " + mensaje);
            
            interfaz.confirmarMovimiento(fila, col);
            
        } catch (Exception e) {
            System.err.println("❌ Error realizando movimiento: " + e.getMessage());
            InterfazConsola.debug("Error en realizarMovimiento: " + e.toString());
        }
    }
    
    /**
     * Cierra la conexión de forma ordenada
     * @param error true si se cierra por error, false si es cierre normal
     */
    public void cerrarConexion(boolean error) {
        if (!conectado) {
            return; // Ya estaba cerrado
        }
        
        conectado = false;
        InterfazConsola.debug("Cerrando conexión... (error: " + error + ")");
        
        try {
            // Esperar un poco antes de cerrar si no es error
            if (!error) {
                Thread.sleep(3000);
            }
            
            // Cerrar recursos en orden
            if (interfaz != null) {
                interfaz.cerrar();
            }
            
            if (in != null) {
                in.close();
                InterfazConsola.debug("BufferedReader cerrado");
            }
            
            if (out != null) {
                out.close();
                InterfazConsola.debug("PrintWriter cerrado");
            }
            
            if (socket != null && !socket.isClosed()) {
                socket.close();
                InterfazConsola.debug("Socket cerrado");
            }
            
            // Interrumpir el hilo receptor si existe
            if (hiloReceptor != null && hiloReceptor.isAlive()) {
                hiloReceptor.interrupt();
                InterfazConsola.debug("Hilo receptor interrumpido");
            }
            
            interfaz.mostrarDesconexion();
            
        } catch (Exception e) {
            System.err.println("❌ Error cerrando conexión: " + e.getMessage());
            InterfazConsola.debug("Error en cerrarConexion: " + e.toString());
        } finally {
            // Salir del programa
            int codigoSalida = error ? 1 : 0;
            InterfazConsola.debug("Saliendo con código: " + codigoSalida);
            System.exit(codigoSalida);
        }
    }
    
    /**
     * Verifica si el cliente está conectado
     */
    public boolean estaConectado() {
        return conectado && socket != null && !socket.isClosed();
    }
    
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 12345;
        
        // Permitir argumentos de línea de comandos
        if (args.length >= 1) {
            host = args[0];
        }
        if (args.length >= 2) {
            try {
                puerto = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("⚠️  Puerto inválido, usando 12345 por defecto");
            }
        }
        
        try {
            InterfazConsola.debug("Iniciando cliente - Host: " + host + ", Puerto: " + puerto);
            Cliente cliente = new Cliente(host, puerto);
            cliente.conectarConServidor();
            
            // Mantener el programa vivo mientras el hilo receptor trabaja
            while (cliente.estaConectado()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    InterfazConsola.debug("Hilo principal interrumpido");
                    break;
                }
            }
            
        } catch (IOException e) {
            System.err.println("\n❌ No se pudo conectar al servidor");
            System.err.println("➤ Asegúrate de que el servidor esté ejecutándose en " + host + ":" + puerto);
            System.err.println("➤ Error: " + e.getMessage());
            InterfazConsola.debug("Error fatal: " + e.toString());
            System.exit(1);
        }
    }
}