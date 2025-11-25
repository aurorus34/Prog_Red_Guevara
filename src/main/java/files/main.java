package files;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class main {
    
    private static final int TAMAÑO_VECTOR = 5;
    private double[] vectorMemoria;
    private String archivoEntrada;
    private String archivoResultados;
    private String archivoErrores;
    private PrintStream ps;
    

    public main() {
        this.vectorMemoria = new double[TAMAÑO_VECTOR];
        this.archivoEntrada = "datos_entrada.txt";
        this.archivoResultados = "resultados.txt";
        this.archivoErrores = "error.txt";
        this.ps = new PrintStream(System.out);
    }
    
    /**
     Punto 1: Ingresa datos numéricos desde diferentes orígenes
     Almacena en memoria volátil (vector) y no-volátil (archivo)
     */
    public void ingresarDatos() {
        ps.println("=== PUNTO 1: INGRESO DE DATOS ===");
        
        // Origen 1: Datos predefinidos para el vector (memoria volátil)
        double[] datosOrigen1 = {10.5, 0, 20.0, 0, 15.5}; // Incluye 2 ceros como requiere
        
        // Cargar vector en memoria
        for (int i = 0; i < TAMAÑO_VECTOR && i < datosOrigen1.length; i++) {
            vectorMemoria[i] = datosOrigen1[i];
        }
        
        ps.println("Vector cargado en memoria volátil:");
        mostrarVector();
        
        // Origen 2: Datos para archivo (memoria no-volátil)
        double[] datosOrigen2 = {25.0, 0, 8.5, 0, 12.3}; // Incluye 2 ceros como requiere
        
        // Guardar en archivo
        guardarDatosEnArchivo(datosOrigen2, archivoEntrada);
        ps.println("Datos guardados en archivo: " + archivoEntrada);
    }
    
    /**
     Punto 2: Lee datos del vector y genera archivos de resultados y errores
     División: dato actual / (siguiente dato - 3)
     */
    public void procesarDatosVector() {
        ps.println("\n=== PUNTO 2: PROCESAMIENTO DATOS DEL VECTOR ===");
        
        List<String> resultados = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        
        for (int i = 0; i < vectorMemoria.length - 1; i++) {
            double numero1 = vectorMemoria[i];
            double numero2 = vectorMemoria[i + 1] - 3; // siguiente número - 3
            
            try {
                if (numero2 == 0) {
                    throw new ArithmeticException("División por cero");
                }
                
                double resultado = numero1 / numero2;
                String linea = String.format("%.2f / %.2f = %.2f", numero1, numero2, resultado);
                resultados.add(linea);
                ps.println("Cálculo exitoso: " + linea);
                
            } catch (ArithmeticException e) {
                String error = String.format("%.2f / %.2f = ERROR: %s", numero1, numero2, e.getMessage());
                errores.add(error);
                ps.println("Error capturado: " + error);
            }
        }
        
        // Guardar resultados y errores en archivos
        escribirArchivo(resultados, archivoResultados);
        escribirArchivo(errores, archivoErrores);
        
        ps.println("Archivos generados: " + archivoResultados + " y " + archivoErrores);
    }
    
    /**
     * Punto 3: Lee datos de ambos orígenes y los divide por 3
     */
    public void procesarDatosAmbosOrigenes() {
        ps.println("\n=== PUNTO 3: PROCESAMIENTO DATOS DE AMBOS ORÍGENES ===");
        
        List<String> resultados = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        
        // Procesar datos del vector (memoria volátil)
        ps.println("Procesando datos del vector:");
        for (double numero : vectorMemoria) {
            procesarDivisionPorTres(numero, 3.0, resultados, errores);
        }
        
        // Procesar datos del archivo (memoria no-volátil)
        ps.println("Procesando datos del archivo:");
        List<Double> datosArchivo = leerDatosDeArchivo(archivoEntrada);
        for (Double numero : datosArchivo) {
            if (numero != null) {
                procesarDivisionPorTres(numero, 3.0, resultados, errores);
            } else {
                String error = "null / 3.0 = ERROR: NullPointerException - Número faltante";
                errores.add(error);
                ps.println("Error capturado: " + error);
            }
        }
        
        // Guardar resultados finales
        escribirArchivo(resultados, "resultados_final.txt");
        escribirArchivo(errores, "error_final.txt");
        
        ps.println("Archivos finales generados: resultados_final.txt y error_final.txt");
    }
    
    /**
     * Procesa la división de un número por 3 y maneja excepciones
     */
    private void procesarDivisionPorTres(Double numero1, double numero2, 
                                       List<String> resultados, List<String> errores) {
        try {
            if (numero1 == null) {
                throw new NullPointerException("Número faltante");
            }
            
            if (numero2 == 0) {
                throw new ArithmeticException("División por cero");
            }
            
            double resultado = numero1 / numero2;
            String linea = String.format("%.2f / %.2f = %.2f", numero1, numero2, resultado);
            resultados.add(linea);
            ps.println("Cálculo exitoso: " + linea);
            
        } catch (ArithmeticException e) {
            String error = String.format("%.2f / %.2f = ERROR: %s", numero1, numero2, e.getMessage());
            errores.add(error);
            ps.println("Error capturado: " + error);
            
        } catch (NullPointerException e) {
            String error = "null / " + numero2 + " = ERROR: " + e.getMessage();
            errores.add(error);
            ps.println("Error capturado: " + error);
        }
    }
    
    /**
     * Guarda un array de datos en un archivo de texto
     */
    private void guardarDatosEnArchivo(double[] datos, String nombreArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (double dato : datos) {
                bw.write(String.valueOf(dato));
                bw.newLine();
            }
        } catch (IOException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, 
                "Error al guardar datos en archivo", e);
        }
    }
    
    /**
     * Lee datos numéricos de un archivo de texto
     */
    private List<Double> leerDatosDeArchivo(String nombreArchivo) {
        List<Double> datos = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try {
                    if (!linea.trim().isEmpty()) {
                        datos.add(Double.parseDouble(linea.trim()));
                    }
                } catch (NumberFormatException e) {
                    ps.println("Línea inválida ignorada: " + linea);
                    datos.add(null); // Añadir null para provocar NullPointerException
                }
            }
        } catch (IOException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, 
                "Error al leer datos del archivo", e);
        }
        
        return datos;
    }
    
    /**
     * Escribe una lista de strings en un archivo
     */
    private void escribirArchivo(List<String> contenido, String nombreArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (String linea : contenido) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, 
                "Error al escribir archivo", e);
        }
    }
    
    /**
     * Muestra el contenido del vector en consola
     */
    private void mostrarVector() {
        ps.print("Vector: [");
        for (int i = 0; i < vectorMemoria.length; i++) {
            ps.print(vectorMemoria[i]);
            if (i < vectorMemoria.length - 1) ps.print(", ");
        }
        ps.println("]");
    }
    
    /**
     * Muestra el estado de los archivos generados
     */
    public void mostrarEstadoArchivos() {
        ps.println("\n=== ESTADO DE ARCHIVOS GENERADOS ===");
        
        String[] archivos = {archivoEntrada, archivoResultados, archivoErrores, 
                           "resultados_final.txt", "error_final.txt"};
        
        for (String archivo : archivos) {
            File f = new File(archivo);
            if (f.exists()) {
                ps.printf("✓ %s - Tamaño: %d bytes%n", archivo, f.length());
            } else {
                ps.printf("✗ %s - No existe%n", archivo);
            }
        }
    }
    
    /**
     * Método principal para ejecutar todo el trabajo práctico
     */
    public static void main(String[] args) {
        main tp = new main();
        
        try {
            // Ejecutar los tres puntos del trabajo práctico
            tp.ingresarDatos();
            tp.procesarDatosVector();
            tp.procesarDatosAmbosOrigenes();
            tp.mostrarEstadoArchivos();
            
            tp.ps.println("\n=== TRABAJO PRÁCTICO COMPLETADO ===");
            
        } catch (Exception e) {
            tp.ps.println("Error general en la ejecución: " + e.getMessage());
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, 
                "Error en main", e);
        }
    }
}