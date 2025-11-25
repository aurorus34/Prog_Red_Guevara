package tp2;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class main {
    
    // Constantes para colores en consola
    public static final String RESET = "\033[0m";
    public static final String ROJO = "\033[31m";
    public static final String VERDE = "\033[32m";
    public static final String AMARILLO = "\033[33m";
    public static final String AZUL = "\033[34m";
    public static final String MAGENTA = "\033[35m";
    public static final String CYAN = "\033[36m";
    public static final String BLANCO = "\033[37m";
    public static final String BOLD = "\033[1m";
    
    private static final String NOMBRE_ARCHIVO = "Inventario.dat";
    private static Scanner scanner = new Scanner(System.in);
    private static File archivoInventario = new File(NOMBRE_ARCHIVO);
    
    public static void main(String[] args) {
        mostrarBienvenida();
        inicializarArchivo();
        menuPrincipal();
    }
    
    //Muestra mensaje de bienvenida al sistema
    private static void mostrarBienvenida() {
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════╗");
        System.out.println("║            SISTEMA DE INVENTARIO              ║");
        System.out.println("║               Versión 1.0                     ║");
        System.out.println("╚═══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    // Inicializar el archivo si no existe
    private static void inicializarArchivo() {
        try {
            if (!archivoInventario.exists()) {
                archivoInventario.createNewFile();
                System.out.println(VERDE + "✓ Archivo de inventario creado: " + NOMBRE_ARCHIVO + RESET);
            }
        } catch (IOException e) {
            System.err.println(ROJO + "Error al crear archivo de inventario: " + e.getMessage() + RESET);
        }
    }
    
    // Menú principal del sistema con bucle infinito
    private static void menuPrincipal() {
        while (true) {
            mostrarMenu();
            String opcion = leerTextoConsola();
            
            switch (opcion) {
                case "1":
                    agregarProducto();
                    break;
                case "2":
                    mostrarProductos();
                    break;
                case "3":
                    editarProducto();
                    break;
                case "4":
                    eliminarProducto();
                    break;
                case "5":
                    System.out.println(AMARILLO + "\n¡Gracias por usar el Sistema de Inventario!" + RESET);
                    System.exit(0);
                    break;
                default:
                    System.out.println(ROJO + "❌ Opción inválida. Por favor seleccione una opción válida." + RESET);
                    presionarEnterParaContinuar();
            }
        }
    }
    

    private static void mostrarMenu() {
        System.out.println("\n" + AZUL + BOLD + "═══════════════ MENÚ PRINCIPAL ═══════════════" + RESET);
        System.out.println(BLANCO + "┌─────────────────────────────────────────────┐");
        System.out.println("│  " + VERDE + "1." + BLANCO + " Agregar Producto                        │");
        System.out.println("│  " + VERDE + "2." + BLANCO + " Mostrar Productos                       │");
        System.out.println("│  " + VERDE + "3." + BLANCO + " Editar Producto                         │");
        System.out.println("│  " + VERDE + "4." + BLANCO + " Eliminar Producto                       │");
        System.out.println("│  " + ROJO + "5." + BLANCO + " Salir                                   │");
        System.out.println("└─────────────────────────────────────────────┘" + RESET);
        System.out.print(CYAN + "Seleccione una opción: " + RESET);
    }
    
    // Lee texto desde la consola y lo devuelve como String
    public static String leerTextoConsola() {
        return scanner.nextLine().trim();
    }
    
    /**
      Identifica el tipo de número en un texto
      @param texto Texto a analizar
      @return 0 si no es número, 1 si es entero, 2 si es decimal
     */
    public static int identificarTipoNumero(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return 0; // No es número
        }
        
        texto = texto.trim();
        
        try {
            // Intentar convertir a entero
            Integer.parseInt(texto);
            return 1; // Es entero
        } catch (NumberFormatException e1) {
            try {
                // Intentar convertir a decimal
                Float.parseFloat(texto.replace(',', '.'));
                return 2; // Es decimal
            } catch (NumberFormatException e2) {
                return 0; // No es número
            }
        }
    }
    
    /**
     * Convierte texto a número entero
     * @param texto Texto a convertir
     * @return Número entero o 0 si no se puede convertir
     */
    public static int convertirAEntero(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Convierte texto a número decimal
     * @param texto Texto a convertir
     * @return Número decimal o 0.0 si no se puede convertir
     */
    public static float convertirADecimal(String texto) {
        try {
            return Float.parseFloat(texto.replace(',', '.').trim());
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }
    
    // Agrega un nuevo producto al inventario
    private static void agregarProducto() {
        System.out.println("\n" + MAGENTA + BOLD + "═══════════════ AGREGAR PRODUCTO ═══════════════" + RESET);
        
        // Solicitar nombre del producto
        System.out.print(CYAN + "Ingrese el nombre del producto: " + RESET);
        String nombre = leerTextoConsola();
        
        if (nombre.isEmpty()) {
            System.out.println(ROJO + "❌ El nombre del producto no puede estar vacío." + RESET);
            presionarEnterParaContinuar();
            return;
        }
        
        // Solicitar precio de compra
        float precioCompra = solicitarPrecio("Ingrese el precio de compra");
        if (precioCompra < 0) return;
        
        // Solicitar precio de venta
        float precioVenta = solicitarPrecio("Ingrese el precio de venta");
        if (precioVenta < 0) return;
        
        // Solicitar stock
        int stock = solicitarStock();
        if (stock < 0) return;
        
        // Crear registro del producto
        String registro = nombre + ";" + precioCompra + ";" + precioVenta + ";" + stock;
        
        // Agregar al archivo
        if (agregarRegistroArchivo(registro)) {
            System.out.println(VERDE + "✓ Producto agregado exitosamente!" + RESET);
        } else {
            System.out.println(ROJO + "❌ Error al agregar el producto." + RESET);
        }
        
        presionarEnterParaContinuar();
    }
    
    // Solicita y valida un precio
    private static float solicitarPrecio(String mensaje) {
        while (true) {
            System.out.print(CYAN + mensaje + ": $" + RESET);
            String entrada = leerTextoConsola();
            
            int tipoNumero = identificarTipoNumero(entrada);
            if (tipoNumero == 0) {
                System.out.println(ROJO + "❌ Debe ingresar un precio válido." + RESET);
                continue;
            }
            
            float precio = convertirADecimal(entrada);
            if (precio < 0) {
                System.out.println(ROJO + "❌ El precio no puede ser negativo." + RESET);
                continue;
            }
            
            return precio;
        }
    }
    
    // Solicita y valida el stock
    private static int solicitarStock() {
        while (true) {
            System.out.print(CYAN + "Ingrese el stock: " + RESET);
            String entrada = leerTextoConsola();
            
            int tipoNumero = identificarTipoNumero(entrada);
            if (tipoNumero != 1) {
                System.out.println(ROJO + "❌ El stock debe ser un número entero." + RESET);
                continue;
            }
            
            int stock = convertirAEntero(entrada);
            if (stock < 0) {
                System.out.println(ROJO + "❌ El stock no puede ser negativo." + RESET);
                continue;
            }
            
            return stock;
        }
    }
    

    private static boolean agregarRegistroArchivo(String registro) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoInventario, true))) {
            writer.write(registro);
            writer.newLine();
            return true;
        } catch (IOException e) {
            Logger.getLogger(main.class.getName()).log(Level.WARNING, null, e);
            return false;
        }
    }
    

    private static void mostrarProductos() {
        System.out.println("\n" + MAGENTA + BOLD + "═══════════════ INVENTARIO ═══════════════" + RESET);
        
        List<String[]> productos = leerProductosArchivo();
        
        if (productos.isEmpty()) {
            System.out.println(AMARILLO + "⚠️  No hay productos en el inventario." + RESET);
            presionarEnterParaContinuar();
            return;
        }
        
        // Encabezado de la tabla
        System.out.println(AZUL + "┌─────┬────────────────────┬─────────────┬─────────────┬─────────┐");
        System.out.println("│ #   │ Producto           │ P. Compra   │ P. Venta    │ Stock   │");
        System.out.println("├─────┼────────────────────┼─────────────┼─────────────┼─────────┤" + RESET);
        
        // Mostrar productos
        for (int i = 0; i < productos.size(); i++) {
            String[] producto = productos.get(i);
            if (producto.length >= 4) {
                System.out.printf(BLANCO + "│ %-3d │ %-18s │ $%-10s │ $%-10s │ %-7s │%s\n", 
                    i + 1, 
                    truncarTexto(producto[0], 18),
                    producto[1], 
                    producto[2], 
                    producto[3],
                    RESET);
            }
        }
        
        System.out.println(AZUL + "└─────┴────────────────────┴─────────────┴─────────────┴─────────┘" + RESET);
        System.out.println(VERDE + "\nTotal de productos: " + productos.size() + RESET);
        
        presionarEnterParaContinuar();
    }
    

    private static String truncarTexto(String texto, int maxLongitud) {
        if (texto.length() <= maxLongitud) {
            return texto;
        }
        return texto.substring(0, maxLongitud - 3) + "...";
    }
    

    private static List<String[]> leerProductosArchivo() {
        List<String[]> productos = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoInventario))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split(";");
                    if (partes.length >= 4) {
                        productos.add(partes);
                    }
                }
            }
        } catch (IOException e) {
            Logger.getLogger(main.class.getName()).log(Level.WARNING, null, e);
        }
        
        return productos;
    }
    

    private static void editarProducto() {
        System.out.println("\n" + MAGENTA + BOLD + "═══════════════ EDITAR PRODUCTO ═══════════════" + RESET);
        
        List<String[]> productos = leerProductosArchivo();
        
        if (productos.isEmpty()) {
            System.out.println(AMARILLO + "⚠️  No hay productos para editar." + RESET);
            presionarEnterParaContinuar();
            return;
        }
        
        // Mostrar productos numerados
        mostrarProductosNumerados(productos);
        
        System.out.print(CYAN + "Seleccione el número del producto a editar: " + RESET);
        String entrada = leerTextoConsola();
        
        int indice = convertirAEntero(entrada) - 1;
        
        if (indice < 0 || indice >= productos.size()) {
            System.out.println(ROJO + "❌ Número de producto inválido." + RESET);
            presionarEnterParaContinuar();
            return;
        }
        
        String[] producto = productos.get(indice);
        
        // Editar campos
        System.out.println(VERDE + "\nEditando producto: " + producto[0] + RESET);
        System.out.println(AMARILLO + "Presione Enter para mantener el valor actual" + RESET);
        
        // Nuevo nombre
        System.out.print(CYAN + "Nombre actual: " + producto[0] + "\nNuevo nombre: " + RESET);
        String nuevoNombre = leerTextoConsola();
        if (!nuevoNombre.isEmpty()) {
            producto[0] = nuevoNombre;
        }
        
        // Nuevo precio de compra
        System.out.print(CYAN + "Precio compra actual: $" + producto[1] + "\nNuevo precio compra: $" + RESET);
        String nuevoPrecioCompra = leerTextoConsola();
        if (!nuevoPrecioCompra.isEmpty() && identificarTipoNumero(nuevoPrecioCompra) > 0) {
            producto[1] = String.valueOf(convertirADecimal(nuevoPrecioCompra));
        }
        
        // Nuevo precio de venta
        System.out.print(CYAN + "Precio venta actual: $" + producto[2] + "\nNuevo precio venta: $" + RESET);
        String nuevoPrecioVenta = leerTextoConsola();
        if (!nuevoPrecioVenta.isEmpty() && identificarTipoNumero(nuevoPrecioVenta) > 0) {
            producto[2] = String.valueOf(convertirADecimal(nuevoPrecioVenta));
        }
        
        // Nuevo stock
        System.out.print(CYAN + "Stock actual: " + producto[3] + "\nNuevo stock: " + RESET);
        String nuevoStock = leerTextoConsola();
        if (!nuevoStock.isEmpty() && identificarTipoNumero(nuevoStock) == 1) {
            producto[3] = String.valueOf(convertirAEntero(nuevoStock));
        }
        
        // Guardar cambios
        if (actualizarArchivo(productos)) {
            System.out.println(VERDE + "✓ Producto editado exitosamente!" + RESET);
        } else {
            System.out.println(ROJO + "❌ Error al editar el producto." + RESET);
        }
        
        presionarEnterParaContinuar();
    }
    

    private static void eliminarProducto() {
        System.out.println("\n" + MAGENTA + BOLD + "═══════════════ ELIMINAR PRODUCTO ═══════════════" + RESET);
        
        List<String[]> productos = leerProductosArchivo();
        
        if (productos.isEmpty()) {
            System.out.println(AMARILLO + "⚠️  No hay productos para eliminar." + RESET);
            presionarEnterParaContinuar();
            return;
        }
        
        // Mostrar productos numerados
        mostrarProductosNumerados(productos);
        
        System.out.print(CYAN + "Seleccione el número del producto a eliminar: " + RESET);
        String entrada = leerTextoConsola();
        
        int indice = convertirAEntero(entrada) - 1;
        
        if (indice < 0 || indice >= productos.size()) {
            System.out.println(ROJO + "❌ Número de producto inválido." + RESET);
            presionarEnterParaContinuar();
            return;
        }
        
        String[] producto = productos.get(indice);
        
        // Confirmar eliminación
        System.out.print(ROJO + "¿Está seguro que desea eliminar '" + producto[0] + "'? (s/N): " + RESET);
        String confirmacion = leerTextoConsola();
        
        if (confirmacion.equalsIgnoreCase("s") || confirmacion.equalsIgnoreCase("si")) {
            productos.remove(indice);
            
            if (actualizarArchivo(productos)) {
                System.out.println(VERDE + "✓ Producto eliminado exitosamente!" + RESET);
            } else {
                System.out.println(ROJO + "❌ Error al eliminar el producto." + RESET);
            }
        } else {
            System.out.println(AMARILLO + "Eliminación cancelada." + RESET);
        }
        
        presionarEnterParaContinuar();
    }
    

    private static void mostrarProductosNumerados(List<String[]> productos) {
        System.out.println(AZUL + "┌─────┬────────────────────┬─────────────┬─────────────┬─────────┐");
        System.out.println("│ #   │ Producto           │ P. Compra   │ P. Venta    │ Stock   │");
        System.out.println("├─────┼────────────────────┼─────────────┼─────────────┼─────────┤" + RESET);
        
        for (int i = 0; i < productos.size(); i++) {
            String[] producto = productos.get(i);
            if (producto.length >= 4) {
                System.out.printf(BLANCO + "│ %-3d │ %-18s │ $%-10s │ $%-10s │ %-7s │%s\n", 
                    i + 1, 
                    truncarTexto(producto[0], 18),
                    producto[1], 
                    producto[2], 
                    producto[3],
                    RESET);
            }
        }
        
        System.out.println(AZUL + "└─────┴────────────────────┴─────────────┴─────────────┴─────────┘" + RESET);
    }
    

    private static boolean actualizarArchivo(List<String[]> productos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoInventario))) {
            for (String[] producto : productos) {
                writer.write(String.join(";", producto));
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            Logger.getLogger(main.class.getName()).log(Level.WARNING, null, e);
            return false;
        }
    }
    

    private static void presionarEnterParaContinuar() {
        System.out.print(AMARILLO + "\nPresione Enter para continuar..." + RESET);
        scanner.nextLine();
    }
}