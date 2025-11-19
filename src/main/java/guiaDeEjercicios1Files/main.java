package guiaDeEjercicios1Files;

import java.io.*;
import java.util.*;

public class main {
    

    public static String leerLinea() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
    
    // Ejercicio 1: Guardar solo el último dato ingresado
    public static void ejercicio1() {
        try {
            System.out.println("=== Ejercicio 1: Último dato ingresado ===");
            String ultimoDato = "";
            String input;
            
            System.out.println("Ingrese datos (escriba 'fin' para terminar):");
            while (true) {
                input = leerLinea();
                if (input.equals("fin")) break;
                ultimoDato = input;
            }
            
            // Crear archivo y escribir último dato
            PrintWriter writer = new PrintWriter(new FileWriter("ultimo_dato.txt"));
            writer.println(ultimoDato);
            writer.close();
            
            System.out.println("Último dato guardado en 'ultimo_dato.txt': " + ultimoDato);
            
        } catch (IOException e) {
            System.err.println("Error en ejercicio 1: " + e.getMessage());
        }
    }
    
    // Ejercicio 2: Guardar todos los valores numéricos
    public static void ejercicio2() {
        try {
            System.out.println("\n=== Ejercicio 2: Valores numéricos ===");
            PrintWriter writer = new PrintWriter(new FileWriter("numeros_usuario.txt"));
            String input;
            
            System.out.println("Ingrese datos (escriba 'fin' para terminar):");
            while (true) {
                input = leerLinea();
                if (input.equals("fin")) break;
                
                try {
                    Double.parseDouble(input);
                    writer.println(input);
                } catch (NumberFormatException e) {
                    System.out.println("'" + input + "' no es un número, se omite.");
                }
            }
            
            writer.close();
            System.out.println("Números guardados en 'numeros_usuario.txt'");
            
        } catch (IOException e) {
            System.err.println("Error en ejercicio 2: " + e.getMessage());
        }
    }
    
    // Ejercicio 3: Crear archivo con números pares del 0 al 1000
    public static void ejercicio3() {
        try {
            System.out.println("\n=== Ejercicio 3: Números pares 0-1000 ===");
            File file = new File("../numeros.txt");
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            
            for (int i = 0; i <= 1000; i += 2) {
                writer.println(i);
            }
            
            writer.close();
            System.out.println("Archivo 'numeros.txt' creado con números pares del 0 al 1000");
            
        } catch (IOException e) {
            System.err.println("Error en ejercicio 3: " + e.getMessage());
        }
    }
    
    // Ejercicio 4: Leer y mostrar números.txt
    public static void ejercicio4() {
        try {
            System.out.println("\n=== Ejercicio 4: Leer numeros.txt ===");
            File file = new File("../numeros.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            String line;
            System.out.println("Contenido de numeros.txt:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            reader.close();
            
        } catch (IOException e) {
            System.err.println("Error en ejercicio 4: " + e.getMessage());
        }
    }
    
    // Ejercicio 5: Eliminar múltiplos de 3 del archivo
    public static void ejercicio5() {
        try {
            System.out.println("\n=== Ejercicio 5: Eliminar múltiplos de 3 ===");
            File file = new File("../numeros.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            ArrayList<String> lineas = new ArrayList<>();
            String line;
            
            while ((line = reader.readLine()) != null) {
                int numero = Integer.parseInt(line.trim());
                if (numero % 3 != 0) {
                    lineas.add(line);
                }
            }
            reader.close();
            
            // Reescribir archivo sin múltiplos de 3
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            for (String linea : lineas) {
                writer.println(linea);
            }
            writer.close();
            
            System.out.println("Múltiplos de 3 eliminados del archivo");
            
        } catch (IOException e) {
            System.err.println("Error en ejercicio 5: " + e.getMessage());
        }
    }
    
    // Método auxiliar para verificar si un número es primo
    public static boolean esPrimo(int numero) {
        if (numero <= 1) return false;
        if (numero <= 3) return true;
        if (numero % 2 == 0 || numero % 3 == 0) return false;
        
        for (int i = 5; i * i <= numero; i += 6) {
            if (numero % i == 0 || numero % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    
    // Ejercicio 6: Crear archivo con números primos
    public static void ejercicio6() {
        try {
            System.out.println("\n=== Ejercicio 6: Números primos ===");
            File sourceFile = new File("../numeros.txt");
            File destFile = new File("../../primos.dat");
            
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            PrintWriter writer = new PrintWriter(new FileWriter(destFile));
            
            String line;
            while ((line = reader.readLine()) != null) {
                int numero = Integer.parseInt(line.trim());
                if (esPrimo(numero)) {
                    writer.println(numero);
                }
            }
            
            reader.close();
            writer.close();
            
            System.out.println("Números primos guardados en 'primos.dat'");
            
        } catch (IOException e) {
            System.err.println("Error en ejercicio 6: " + e.getMessage());
        }
    }
    
    // Ejercicio 7: Manejo de palabras con 'ñ'
    public static void ejercicio7() {
        try {
            System.out.println("\n=== Ejercicio 7: Palabras con 'ñ' ===");
            File file = new File("../caracteres.dat");
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            
            System.out.println("Ingrese 10 palabras que contengan la letra 'ñ':");
            for (int i = 0; i < 10; i++) {
                System.out.print("Palabra " + (i + 1) + ": ");
                String palabra = leerLinea();
                writer.println(palabra);
            }
            writer.close();
            
            // Leer y mostrar fichero original
            BufferedReader reader = new BufferedReader(new FileReader(file));
            ArrayList<String> palabras = new ArrayList<>();
            String line;
            
            System.out.println("\nFichero original:");
            while ((line = reader.readLine()) != null) {
                palabras.add(line);
                System.out.println(line);
            }
            reader.close();
            
            // Reemplazar 'ñ' por 'nie-nio' y reescribir
            PrintWriter writerFixed = new PrintWriter(new FileWriter(file));
            System.out.println("\nFichero arreglado:");
            for (String palabra : palabras) {
                String palabraArreglada = palabra.replace("ñ", "nie-nio");
                writerFixed.println(palabraArreglada);
                System.out.println(palabraArreglada);
            }
            writerFixed.close();
            
        } catch (IOException e) {
            System.err.println("Error en ejercicio 7: " + e.getMessage());
        }
    }
    
    // Ejercicio 8: Eliminar lorem de archivo HTML
    public static void ejercicio8() {
        try {
            System.out.println("\n=== Ejercicio 8: Eliminar lorem de HTML ===");
            
            // Crear archivo HTML con lorem
            File htmlFile = new File("../ejemplo.html");
            PrintWriter writer = new PrintWriter(new FileWriter(htmlFile));
            writer.println("<!DOCTYPE html>");
            writer.println("<html>");
            writer.println("<head><title>Ejemplo</title></head>");
            writer.println("<body>");
            writer.println("<h1>Mi página web</h1>");
            writer.println("<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>");
            writer.println("<p>Contenido importante aquí.</p>");
            writer.println("<p>Más lorem ipsum text aquí también.</p>");
            writer.println("</body>");
            writer.println("</html>");
            writer.close();
            
            // Leer archivo y eliminar líneas que contengan "lorem"
            BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
            ArrayList<String> lineas = new ArrayList<>();
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (!line.toLowerCase().contains("lorem")) {
                    lineas.add(line);
                }
            }
            reader.close();
            
            // Reescribir archivo sin lorem
            PrintWriter writerClean = new PrintWriter(new FileWriter(htmlFile));
            for (String linea : lineas) {
                writerClean.println(linea);
            }
            writerClean.close();
            
            System.out.println("Archivo HTML limpiado de contenido 'lorem'");
            
        } catch (IOException e) {
            System.err.println("Error en ejercicio 8: " + e.getMessage());
        }
    }
    
    // Clase para manejo de datos climáticos
    static class DatoClima {
        String fecha;
        double temperatura;
        String descripcion;
        
        public DatoClima(String fecha, double temperatura, String descripcion) {
            this.fecha = fecha;
            this.temperatura = temperatura;
            this.descripcion = descripcion;
        }
        
        @Override
        public String toString() {
            return fecha + "," + temperatura + "," + descripcion;
        }
        
        public String mostrar() {
            return "Fecha: " + fecha + " - Temp: " + temperatura + "°C - " + descripcion;
        }
    }
    
    // Ejercicio 9: Sistema de datos climáticos
    public static void ejercicio9() {
        try {
            System.out.println("\n=== Ejercicio 9: Sistema de datos climáticos ===");
            File climaFile = new File("../datos_clima.txt");
            ArrayList<DatoClima> datos = new ArrayList<>();
            
            // Cargar datos existentes si el archivo existe
            if (climaFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(climaFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] partes = line.split(",");
                    if (partes.length == 3) {
                        datos.add(new DatoClima(partes[0], Double.parseDouble(partes[1]), partes[2]));
                    }
                }
                reader.close();
            }
            
            String opcion;
            do {
                System.out.println("\n--- Menú Datos Climáticos ---");
                System.out.println("1. Agregar dato climático");
                System.out.println("2. Mostrar todos los datos");
                System.out.println("3. Borrar registro");
                System.out.println("4. Salir");
                System.out.print("Seleccione opción: ");
                
                opcion = leerLinea();
                
                switch (opcion) {
                    case "1":
                        System.out.print("Ingrese fecha (dd/mm/yyyy): ");
                        String fecha = leerLinea();
                        System.out.print("Ingrese temperatura: ");
                        double temp = Double.parseDouble(leerLinea());
                        System.out.print("Ingrese descripción: ");
                        String desc = leerLinea();
                        
                        datos.add(new DatoClima(fecha, temp, desc));
                        System.out.println("Dato agregado correctamente");
                        break;
                        
                    case "2":
                        if (datos.isEmpty()) {
                            System.out.println("No hay datos registrados");
                        } else {
                            System.out.println("Datos climáticos registrados:");
                            for (int i = 0; i < datos.size(); i++) {
                                System.out.println((i + 1) + ". " + datos.get(i).mostrar());
                            }
                        }
                        break;
                        
                    case "3":
                        if (datos.isEmpty()) {
                            System.out.println("No hay datos para borrar");
                        } else {
                            System.out.println("Seleccione el número del registro a borrar:");
                            for (int i = 0; i < datos.size(); i++) {
                                System.out.println((i + 1) + ". " + datos.get(i).mostrar());
                            }
                            System.out.print("Número: ");
                            int indice = Integer.parseInt(leerLinea()) - 1;
                            if (indice >= 0 && indice < datos.size()) {
                                datos.remove(indice);
                                System.out.println("Registro eliminado");
                            } else {
                                System.out.println("Índice inválido");
                            }
                        }
                        break;
                        
                    case "4":
                        System.out.println("Saliendo...");
                        break;
                        
                    default:
                        System.out.println("Opción inválida");
                }
                
            } while (!opcion.equals("4"));
            
            // Guardar datos en archivo
            PrintWriter writer = new PrintWriter(new FileWriter(climaFile));
            for (DatoClima dato : datos) {
                writer.println(dato.toString());
            }
            writer.close();
            
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error en ejercicio 9: " + e.getMessage());
        }
    }
    
    // EJERCICIOS DE COLECCIONES
    
    // Ejercicio 10: Lista de enteros con estadísticas
    public static List<Integer> leerValores() {
        try {
            List<Integer> numeros = new ArrayList<>();
            System.out.println("\n=== Ejercicio 10: Lista de enteros ===");
            System.out.println("Ingrese números enteros (-99 para terminar):");
            
            String input;
            while (true) {
                input = leerLinea();
                try {
                    int numero = Integer.parseInt(input);
                    if (numero == -99) break;
                    numeros.add(numero);
                } catch (NumberFormatException e) {
                    System.out.println("Por favor ingrese un número entero válido");
                }
            }
            
            return numeros;
            
        } catch (IOException e) {
            System.err.println("Error leyendo valores: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public static int calcularSuma(List<Integer> numeros) {
        int suma = 0;
        for (Integer num : numeros) {
            suma += num;
        }
        return suma;
    }
    
    public static void mostrarResultados(List<Integer> numeros) {
        int suma = calcularSuma(numeros);
        double media = numeros.isEmpty() ? 0 : (double) suma / numeros.size();
        
        System.out.println("Cantidad de valores: " + numeros.size());
        System.out.println("Suma: " + suma);
        System.out.println("Media: " + media);
        
        System.out.println("Valores leídos:");
        int mayoresQueMedia = 0;
        for (Integer num : numeros) {
            System.out.print(num + " ");
            if (num > media) {
                mayoresQueMedia++;
            }
        }
        System.out.println();
        System.out.println("Números mayores que la media: " + mayoresQueMedia);
    }
    
    public static void ejercicio10() {
        List<Integer> numeros = leerValores();
        mostrarResultados(numeros);
    }
    
    // Ejercicio 11: Clase Colegio
    static class Colegio {
        private Map<String, Integer> nacionalidades;
        
        public Colegio() {
            this.nacionalidades = new HashMap<>();
        }
        
        public void addAlumno(String nacionalidad) {
            nacionalidades.put(nacionalidad, nacionalidades.getOrDefault(nacionalidad, 0) + 1);
        }
        
        public void showAll() {
            System.out.println("Nacionalidades y cantidad de alumnos:");
            for (Map.Entry<String, Integer> entry : nacionalidades.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " alumnos");
            }
        }
        
        public void showNacionalidad(String nacionalidad) {
            int cantidad = nacionalidades.getOrDefault(nacionalidad, 0);
            System.out.println(nacionalidad + ": " + cantidad + " alumnos");
        }
        
        public void cuantos() {
            System.out.println("Cantidad de nacionalidades diferentes: " + nacionalidades.size());
        }
        
        public void borra() {
            nacionalidades.clear();
            System.out.println("Datos eliminados");
        }
    }
    
    public static void ejercicio11() {
        try {
            System.out.println("\n=== Ejercicio 11: Colegio ===");
            Colegio colegio = new Colegio();
            
            // Agregar algunos alumnos de ejemplo
            colegio.addAlumno("Argentina");
            colegio.addAlumno("Brasil");
            colegio.addAlumno("Argentina");
            colegio.addAlumno("Chile");
            colegio.addAlumno("Brasil");
            colegio.addAlumno("Argentina");
            
            colegio.showAll();
            colegio.showNacionalidad("Argentina");
            colegio.cuantos();
            
        } catch (Exception e) {
            System.err.println("Error en ejercicio 11: " + e.getMessage());
        }
    }
    
    // Ejercicio 12: Días de la semana
    public static void ejercicio12() {
        System.out.println("\n=== Ejercicio 12: Días de la semana ===");
        
        // Crear lista con días de la semana
        List<String> listaDias = new ArrayList<>();
        listaDias.add("Lunes");
        listaDias.add("Martes");
        listaDias.add("Miércoles");
        listaDias.add("Jueves");
        listaDias.add("Viernes");
        listaDias.add("Sábado");
        listaDias.add("Domingo");
        
        // Insertar "Juernes" en posición 4
        listaDias.add(4, "Juernes");
        
        // Crear copia
        List<String> listaDos = new ArrayList<>(listaDias);
        
        // Añadir contenido de listaDos a listaDias
        listaDias.addAll(listaDos);
        
        // Mostrar posiciones 3 y 4
        System.out.println("Posición 3: " + listaDias.get(3));
        System.out.println("Posición 4: " + listaDias.get(4));
        
        // Mostrar primer y último elemento
        System.out.println("Primer elemento: " + listaDias.get(0));
        System.out.println("Último elemento: " + listaDias.get(listaDias.size() - 1));
        
        // Eliminar "Juernes"
        boolean eliminado = listaDias.remove("Juernes");
        System.out.println("¿Se eliminó 'Juernes'? " + eliminado);
        
        // Mostrar con Iterator
        System.out.println("Elementos usando Iterator:");
        Iterator<String> it = listaDias.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        
        // Buscar "Lunes" (case insensitive)
        boolean encontrado = false;
        for (String dia : listaDias) {
            if (dia.equalsIgnoreCase("lunes")) {
                encontrado = true;
                break;
            }
        }
        System.out.println("¿Existe 'Lunes'? " + encontrado);
        
        // Ordenar y mostrar
        Collections.sort(listaDias);
        System.out.println("Lista ordenada: " + listaDias);
    }
    
    // Ejercicio 13: Conjunto de jugadores
    public static void ejercicio13() {
        System.out.println("\n=== Ejercicio 13: Conjunto de jugadores ===");
        
        // Crear conjunto de jugadores del FC Barcelona
        Set<String> jugadores = new HashSet<>();
        jugadores.add("Jordi Alba");
        jugadores.add("Pique");
        jugadores.add("Busquets");
        jugadores.add("Iniesta");
        jugadores.add("Messi");
        
        // Mostrar jugadores con Iterator
        System.out.println("Jugadores del FC Barcelona:");
        Iterator<String> it = jugadores.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        
        // Consultar si existe "Neymar JR"
        boolean existeNeymar = jugadores.contains("Neymar JR");
        System.out.println("¿Existe 'Neymar JR'? " + existeNeymar);
        
        // Crear segundo conjunto
        Set<String> jugadores2 = new HashSet<>();
        jugadores2.add("Piqué");
        jugadores2.add("Busquets");
        
        // Verificar si todos los elementos de jugadores2 están en jugadores
        boolean todosExisten = jugadores.containsAll(jugadores2);
        System.out.println("¿Todos los jugadores de jugadores2 existen en jugadores? " + todosExisten);
        
        // Unión de conjuntos
        Set<String> union = new HashSet<>(jugadores);
        union.addAll(jugadores2);
        System.out.println("Unión de conjuntos: " + union);
        
        // Intentar agregar "Piqué" nuevamente (no se permite duplicados)
        boolean agregado = jugadores.add("Piqué");
        System.out.println("¿Se agregó 'Piqué' nuevamente? " + agregado);
    }
    
    // Ejercicio 14: Generador de números de lotería
    public static void ejercicio14() {
        System.out.println("\n=== Ejercicio 14: Generador de lotería ===");
        
        Random random = new Random();
        
        // Generar 6 números rojos únicos (1-33)
        Set<Integer> bolasRojas = new HashSet<>();
        while (bolasRojas.size() < 6) {
            bolasRojas.add(random.nextInt(33) + 1);
        }
        
        // Generar 1 número azul (1-16)
        int bolaAzul = random.nextInt(16) + 1;
        
        System.out.println("Números ganadores:");
        System.out.println("Bolas rojas: " + bolasRojas);
        System.out.println("Bola azul: " + bolaAzul);
    }
    
    // Método principal para ejecutar todos los ejercicios
    public static void main(String[] args) {
        try {
            System.out.println("=== EJERCICIOS DE I/O Y COLECCIONES ===\n");
            
            // Ejercicios de I/O
            ejercicio1();
            ejercicio2();
            ejercicio3();
            ejercicio4();
            ejercicio5();
            ejercicio6();
            ejercicio7();
            ejercicio8();
            ejercicio9();
            
            // Ejercicios de Colecciones
            ejercicio10();
            ejercicio11();
            ejercicio12();
            ejercicio13();
            ejercicio14();
            
            System.out.println("\n=== TODOS LOS EJERCICIOS COMPLETADOS ===");
            
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }
}