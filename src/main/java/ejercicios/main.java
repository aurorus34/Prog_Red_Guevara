package ejercicios;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.iniciar();
    }
}

class Menu {
    private EjerciciosSystemIO ejerciciosSystem;
    private EjerciciosReaderIO ejerciciosReader;
    
    public Menu() {
        ejerciciosSystem = new EjerciciosSystemIO();
        ejerciciosReader = new EjerciciosReaderIO();
    }
    
    public void iniciar() {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n==== MENu DE EJERCICIOS I/O ====");
            System.out.println("Ejercicios con Class System:");
            System.out.println("1. Calculadora de sueldo bruto");
            System.out.println("2. Calculadora de ángulo restante de un triangulo");
            System.out.println("3. Calculadora del perímetro de un cuadrado");
            System.out.println("4. Convertor de Fahrenheit a Celsius");
            System.out.println("5. Convertor de segundos a días, horas, minutos y segundos");
            System.out.println("6. Calculadora de planes de pago");
            System.out.println("7. Mes aproximado según signo zodiacal");
            
            System.out.println("\nEjercicios con Class Reader:");
            System.out.println("8. Ordenar apellidos alfabeticamente");
            System.out.println("9. Determinar el menor de cuatro numeros");
            System.out.println("10. Determinar si un numero es par o impar");
            System.out.println("11. Verificar si el mayor es divisible por el menor");
            System.out.println("12. Determinar signo zodiacal según fecha de nacimiento");
            System.out.println("13. Comparar longitud de apellidos");
            System.out.println("14. Tabla de multiplicar");
            System.out.println("15. Verificar si un numero es primo");
            System.out.println("0. Salir\n");
            System.out.print("\nSeleccione una opcion: ");
            
            int opcion = leerEnteroSystem();
            
            switch (opcion) {
                case 0:
                    continuar = false;
                    System.out.println("¡Programa finalizado!");
                    break;
                case 1:
                    ejerciciosSystem.calculoSueldoBruto();
                    break;
                case 2:
                    ejerciciosSystem.calculoAnguloTriangulo();
                    break;
                case 3:
                    ejerciciosSystem.calculoPerimetroCuadrado();
                    break;
                case 4:
                    ejerciciosSystem.conversionTemperatura();
                    break;
                case 5:
                    ejerciciosSystem.conversionTiempo();
                    break;
                case 6:
                    ejerciciosSystem.calculoPlanesArticulo();
                    break;
                case 7:
                    ejerciciosSystem.mesSegunSignoZodiacal();
                    break;
                case 8:
                    ejerciciosReader.ordenarApellidos();
                    break;
                case 9:
                    ejerciciosReader.menorDeCuatroNumeros();
                    break;
                case 10:
                    ejerciciosReader.determinarParImpar();
                    break;
                case 11:
                    ejerciciosReader.verificarDivisibilidad();
                    break;
                case 12:
                    ejerciciosReader.determinarSignoZodiacal();
                    break;
                case 13:
                    ejerciciosReader.compararLongitudApellidos();
                    break;
                case 14:
                    ejerciciosReader.tablaMultiplicar();
                    break;
                case 15:
                    ejerciciosReader.verificarNumeroPrimo();
                    break;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
            }
        }
    }
    
    // metodo para leer enteros con system.in
    // hice este metodo porque me parece mas comodo verificar la lectura dfe enteros de esta forma
    // y no repetir codigo
    private int leerEnteroSystem() {
        int numero = 0;
        boolean entradaValida = false;
        
        while (!entradaValida) {
            try {
                int caracter;
                StringBuilder entrada = new StringBuilder();
                
                
                while ((caracter = System.in.read()) != '\n' && caracter != -1) {
                    if (caracter != '\r') { 
                        entrada.append((char) caracter);
                    }
                }
                
                String input = entrada.toString().trim();
                if (!input.isEmpty()) {
                    numero = Integer.parseInt(input);
                    entradaValida = true;
                } else {
                    System.out.print("Entrada vacía. Intente nuevamente: ");
                }
            } catch (IOException e) {
                System.out.print("Error de lectura. Intente nuevamente: ");
            } catch (NumberFormatException e) {
                System.out.print("Formato inválido. Ingrese un número entero: ");
            }
        }
        
        return numero;
    }
}

class EjerciciosSystemIO {
    // metodo para leer enteros usando System.in
	// hice este metodo porque me parece mas comodo verificar la lectura dfe enteros de esta forma
    // y no repetir codigo
    private int leerEntero() {
        int numero = 0;
        boolean entradaValida = false;
        
        while (!entradaValida) {
            try {
                int caracter;
                StringBuilder entrada = new StringBuilder();
                
                
                while ((caracter = System.in.read()) != '\n' && caracter != -1) {
                    if (caracter != '\r') { 
                        entrada.append((char) caracter);
                    }
                }
                
                String input = entrada.toString().trim();
                if (!input.isEmpty()) {
                    numero = Integer.parseInt(input);
                    entradaValida = true;
                } else {
                    System.out.print("Entrada vacía. Intente nuevamente: ");
                }
            } catch (IOException e) {
                System.out.print("Error de lectura. Intente nuevamente: ");
            } catch (NumberFormatException e) {
                System.out.print("Formato inválido. Ingrese un número entero: ");
            }
        }
        
        return numero;
    }
    
    // metodo para leer doubles usando System.in
 // hice este metodo porque me parece mas comodo verificar la lectura dfe doubles de esta forma
    // y no repetir codigo
    private double leerDouble() {
        double numero = 0;
        boolean entradaValida = false;
        
        while (!entradaValida) {
            try {
                int caracter;
                StringBuilder entrada = new StringBuilder();
                
                
                while ((caracter = System.in.read()) != '\n' && caracter != -1) {
                    if (caracter != '\r') { 
                        entrada.append((char) caracter);
                    }
                }
                
                String input = entrada.toString().trim();
                if (!input.isEmpty()) {
                    numero = Double.parseDouble(input);
                    entradaValida = true;
                } else {
                    System.out.print("Entrada vacía. Intente nuevamente: ");
                }
            } catch (IOException e) {
                System.out.print("Error de lectura. Intente nuevamente: ");
            } catch (NumberFormatException e) {
                System.out.print("Formato inválido. Ingrese un número válido: ");
            }
        }
        
        return numero;
    }
    
    // metodo para leer un string  usando System.in
    // hice este metodo porque me parece mas comodo verificar la lectura dfe enteros de esta forma
    // y no repetir codigo
    private String leerCadena() {
        String cadena = "";
        boolean entradaValida = false;
        
        while (!entradaValida) {
            try {
                int caracter;
                StringBuilder entrada = new StringBuilder();
                
                
                while ((caracter = System.in.read()) != '\n' && caracter != -1) {
                    if (caracter != '\r') { 
                        entrada.append((char) caracter);
                    }
                }
                
                cadena = entrada.toString().trim();
                if (!cadena.isEmpty()) {
                    entradaValida = true;
                } else {
                    System.out.print("Entrada vacía. Intente nuevamente: ");
                }
            } catch (IOException e) {
                System.out.print("Error de lectura. Intente nuevamente: ");
            }
        }
        
        return cadena;
    }

    // 1.1 calculadora sueldo bruto
    public void calculoSueldoBruto() {
        System.out.println("\n=== CÁLCULO DE SUELDO BRUTO ===");
        
        System.out.print("Ingrese el valor de la hora de trabajo: $");
        double valor_hora = leerDouble();
        while (valor_hora <= 0) {
            System.out.print("El valor de la hora debe ser mayor que cero. Ingrese nuevamente: $");
            valor_hora = leerDouble();
        }
        
        System.out.print("Ingrese la cantidad de horas trabajadas: ");
        double horas_trabajadas = leerDouble();
        while (horas_trabajadas < 0) {
            System.out.print("Las horas trabajadas no pueden ser negativas. Ingrese nuevamente: ");
            horas_trabajadas = leerDouble();
        }
        
        double sueldo_bruto = valor_hora * horas_trabajadas;
        System.out.println("El sueldo bruto es: $" + sueldo_bruto);
    }
    
    // 1.2 calculadora angulo restante de un triangulo
    public void calculoAnguloTriangulo() {
        System.out.println("\n=== CALCULADORA DE ANGULO RESTANTE DE UN TRIANGULO ===");
        
        System.out.print("Ingrese el valor del primer angulo interior: ");
        double angulo1 = leerDouble();
        while (angulo1 <= 0 || angulo1 >= 180) {
            System.out.print("El angulo debe estar entre 0 y 180 grados. Ingrese nuevamente: ");
            angulo1 = leerDouble();
        }
        
        System.out.print("Ingrese el valor del segundo angulo interior: ");
        double angulo2 = leerDouble();
        while (angulo2 <= 0 || angulo2 >= 180) {
            System.out.print("El angulo debe estar entre 0 y 180 grados. Ingrese nuevamente: ");
            angulo2 = leerDouble();
        }
        
        while (angulo1 + angulo2 >= 180) {
            System.out.println("La suma de los angulos no puede ser mayor o igual a 180 grados.");
            System.out.print("Ingrese el valor del primer angulo interior: ");
            angulo1 = leerDouble();
            while (angulo1 <= 0 || angulo1 >= 180) {
                System.out.print("El angulo debe estar entre 0 y 180 grados. Ingrese nuevamente: ");
                angulo1 = leerDouble();
            }
            
            System.out.print("Ingrese el valor del segundo angulo interior: ");
            angulo2 = leerDouble();
            while (angulo2 <= 0 || angulo2 >= 180) {
                System.out.print("El angulo debe estar entre 0 y 180 grados. Ingrese nuevamente: ");
                angulo2 = leerDouble();
            }
        }
        
        double angulo3 = 180 - angulo1 - angulo2;
        System.out.println("El tercer angulo interior del triangulo es: " + angulo3 + " grados");
    }
    
    // 1.3 calculadora de perímetro de un cuadrado
    public void calculoPerimetroCuadrado() {
        System.out.println("\n=== CALCULADORA DE PERÍMETRO DE UN CUADRADO ===");
        
        System.out.print("Ingrese la superficie del cuadrado en m²: ");
        double superficie = leerDouble();
        while (superficie <= 0) {
            System.out.print("La superficie debe ser un valor positivo. Ingrese nuevamente: ");
            superficie = leerDouble();
        }
        
        double lado = Math.sqrt(superficie);
        double perimetro = 4 * lado;
        
        System.out.println("El lado del cuadrado es: " + lado + " m");
        System.out.println("El perímetro del cuadrado es: " + perimetro + " m");
    }
    
    // 1.4 convertora de Fahrenheit a Celsius
    public void conversionTemperatura() {
        System.out.println("\n=== CONVERTOR DE FAHRENHEIT A CELSIUS ===");
        
        System.out.print("Ingrese la temperatura en grados Fahrenheit: ");
        double temperaturaF = leerDouble();
        
        double temperaturaC = (temperaturaF - 32) * 5 / 9;
        
        System.out.println(temperaturaF + " grados Fahrenheit equivalen a " + temperaturaC + " grados Celsius");
    }
    
    // 1.5 convertor de segundos a días, horas, minutos y segundos
    public void conversionTiempo() {
        System.out.println("\n=== CONVERSIÓN DE SEGUNDOS A DÍAS, HORAS, MINUTOS Y SEGUNDOS ===");
        
        System.out.print("Ingrese el tiempo en segundos: ");
        int tiempo_en_segundos = leerEntero();
        while (tiempo_en_segundos < 0) {
            System.out.print("El tiempo no puede ser negativo. Ingrese nuevamente: ");
            tiempo_en_segundos = leerEntero();
        }
        
        int segundos_restantes = tiempo_en_segundos;
        
        int dias = segundos_restantes / 86400;  // 24*60*60 segundos en un día
        segundos_restantes %= 86400;
        
        int horas = segundos_restantes / 3600;  // 60*60 segundos en una hora
        segundos_restantes %= 3600;
        
        int minutos = segundos_restantes / 60;
        int segundos = segundos_restantes % 60;
        
        System.out.println(tiempo_en_segundos + " segundos equivalen a:");
        System.out.println(dias + " días, " + horas + " horas, " + minutos + " minutos y " + segundos + " segundos");
    }
    
    // 1.6 calculadora de planes de pago
    public void calculoPlanesArticulo() {
        System.out.println("\n=== CALCULADORA DE PLANES DE PAGO ===");
        
        System.out.print("Ingrese el precio del articulo: $");
        double precio_articulo = leerDouble();
        while (precio_articulo <= 0) {
            System.out.print("El precio debe ser mayor que cero. Ingrese nuevamente: $");
            precio_articulo = leerDouble();
        }
        
        // Plan 1: 100% al contado con 10% de descuento
        double plan1 = precio_articulo * 0.9;
        
        // Plan 2: 50% al contado y 2 cuotas iguales, con incremento del 10%
        double precio_incremento_10 = precio_articulo * 1.1;
        double plan_2_contado = precio_incremento_10 * 0.5;
        double plan_2_cuota = precio_incremento_10 * 0.5 / 2;
        
        // Plan 3: 25% al contado y 5 cuotas iguales, con incremento del 15%
        double precio_incremento_15 = precio_articulo * 1.15;
        double plan_3_contado = precio_incremento_15 * 0.25;
        double plan_3_cuota = precio_incremento_15 * 0.75 / 5;
        
        // Plan 4: 8 cuotas con incremento del 25%
        double precion_incremento_25 = precio_articulo * 1.25;
        double plan_4_cuota_inicial = precion_incremento_25 * 0.6 / 4;
        double plan_4_cuota_final = precion_incremento_25 * 0.4 / 4;
        
        System.out.println("\nOpciones de pago:");
        System.out.println("Plan 1 (100% al contado con 10% de descuento):");
        System.out.println("  Total a pagar: $" + plan1);
        
        System.out.println("\nPlan 2 (50% al contado y 2 cuotas, con incremento del 10%):");
        System.out.println("  Pago inicial al contado: $" + plan_2_contado);
        System.out.println("  2 cuotas de: $" + plan_2_cuota + " cada una");
        System.out.println("  Total a pagar: $" + precio_incremento_10);
        
        System.out.println("\nPlan 3 (25% al contado y 5 cuotas, con incremento del 15%):");
        System.out.println("  Pago inicial al contado: $" + plan_3_contado);
        System.out.println("  5 cuotas de: $" + plan_3_cuota + " cada una");
        System.out.println("  Total a pagar: $" + precio_incremento_15);
        
        System.out.println("\nPlan 4 (8 cuotas, con incremento del 25%):");
        System.out.println("  Primeras 4 cuotas de: $" + plan_4_cuota_inicial + " cada una");
        System.out.println("  Últimas 4 cuotas de: $" + plan_4_cuota_final + " cada una");
        System.out.println("  Total a pagar: $" + precion_incremento_25);
    }
    
    // 1.7 Mes aproximado segun signo zodiacal
    public void mesSegunSignoZodiacal() {
        System.out.println("\n=== MES APROXIMADO SEGÚN SIGNO ZODIACAL ===");
        System.out.println("Signos disponibles: Aries, Tauro, Géminis, Cáncer, Leo, Virgo, Libra, Escorpio, Sagitario, Capricornio, Acuario, Piscis");
        
        System.out.print("Ingrese su signo zodiacal: ");
        String signo = leerCadena().toLowerCase();
        
        boolean signo_valido = false;
        while (!signo_valido) {
        	signo_valido = true;
            
            switch (signo) {
                case "aries":
                    System.out.println("Su mes de nacimiento aproximado es: finales de marzo a mediados de abril");
                    break;
                case "tauro":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de abril a mediados de mayo");
                    break;
                case "geminis":
                case "géminis":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de mayo a mediados de junio");
                    break;
                case "cancer":
                case "cáncer":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de junio a mediados de julio");
                    break;
                case "leo":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de julio a mediados de agosto");
                    break;
                case "virgo":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de agosto a mediados de septiembre");
                    break;
                case "libra":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de septiembre a mediados de octubre");
                    break;
                case "escorpio":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de octubre a mediados de noviembre");
                    break;
                case "sagitario":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de noviembre a mediados de diciembre");
                    break;
                case "capricornio":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de diciembre a mediados de enero");
                    break;
                case "acuario":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de enero a mediados de febrero");
                    break;
                case "piscis":
                    System.out.println("Su mes de nacimiento aproximado es: mediados de febrero a mediados de marzo");
                    break;
                default:
                    System.out.print("Signo zodiacal no válido. Intente nuevamente: ");
                    signo = leerCadena().toLowerCase();
                    signo_valido = false;
            }
        }
    }
}

class EjerciciosReaderIO {
    private Reader inputReader;
    
    public EjerciciosReaderIO() {
        inputReader = new InputStreamReader(System.in);
    }
    
    
    private int leerEntero() {
        int numero = 0;
        boolean entradaValida = false;
        
        while (!entradaValida) {
            try {
                StringBuilder entrada = new StringBuilder();
                int c;
                
                
                while ((c = inputReader.read()) != '\n' && c != -1) {
                    if (c != '\r') { 
                        entrada.append((char) c);
                    }
                }
                
                String input = entrada.toString().trim();
                if (!input.isEmpty()) {
                    numero = Integer.parseInt(input);
                    entradaValida = true;
                } else {
                    System.out.print("Entrada vacia. Intente nuevamente: ");
                }
            } catch (IOException e) {
                System.out.print("Error de lectura. Intente nuevamente: ");
            } catch (NumberFormatException e) {
                System.out.print("Formato invlido. Ingrese un número entero: ");
            }
        }
        
        return numero;
    }
    
 
    private double leerDouble() {
        double numero = 0;
        boolean entradaValida = false;
        
        while (!entradaValida) {
            try {
                StringBuilder entrada = new StringBuilder();
                int c;
                
                while ((c = inputReader.read()) != '\n' && c != -1) {
                    if (c != '\r') { 
                        entrada.append((char) c);
                    }
                }
                
                String input = entrada.toString().trim();
                if (!input.isEmpty()) {
                    numero = Double.parseDouble(input);
                    entradaValida = true;
                } else {
                    System.out.print("Entrada vacía. Intente nuevamente: ");
                }
            } catch (IOException e) {
                System.out.print("Error de lectura. Intente nuevamente: ");
            } catch (NumberFormatException e) {
                System.out.print("Formato inválido. Ingrese un número válido: ");
            }
        }
        
        return numero;
    }
    
    private String leerCadena() {
        String cadena = "";
        boolean entradaValida = false;
        
        while (!entradaValida) {
            try {
                StringBuilder entrada = new StringBuilder();
                int c;
                
                while ((c = inputReader.read()) != '\n' && c != -1) {
                    if (c != '\r') { 
                        entrada.append((char) c);
                    }
                }
                
                cadena = entrada.toString().trim();
                if (!cadena.isEmpty()) {
                    entradaValida = true;
                } else {
                    System.out.print("Entrada vacia. Intente nuevamente: ");
                }
            } catch (IOException e) {
                System.out.print("Error de lectura. Intente nuevamente: ");
            }
        }
        
        return cadena;
    }
    
    // 2.1 ordenar apellidos alfabeticamente
    public void ordenarApellidos() {
        System.out.println("\n=== ORDENAR APELLIDOS ALFABETICAMENTE ===");
        
        System.out.print("Ingrese el primer apellido: ");
        String apellido1 = leerCadena();
        
        System.out.print("Ingrese el segundo apellido: ");
        String apellido2 = leerCadena();
        
        System.out.print("Ingrese el tercer apellido: ");
        String apellido3 = leerCadena();
        
        // bubble sort
        String[] apellidos = {apellido1, apellido2, apellido3};
        
        for (int i = 0; i < apellidos.length - 1; i++) {
        	
            for (int j = 0; j < apellidos.length - i - 1; j++) {
            	
                if (apellidos[j].compareToIgnoreCase(apellidos[j + 1]) > 0) {
                	
                    String temp = apellidos[j];
                    apellidos[j] = apellidos[j + 1];
                    apellidos[j + 1] = temp;
                }
            }
        }
        
        System.out.println("\nApellidos ordenados alfabeticamente:");
        for (String apellido : apellidos) {
            System.out.println(apellido);
        }
    }
    
    // 2.2 determinar el menor de cuatro numeros
    public void menorDeCuatroNumeros() {
        System.out.println("\n=== DETERMINAR EL MENOR DE CUATRO NUMEROS ===");
        
        System.out.print("Ingrese el primer numero: ");
        double num1 = leerDouble();
        
        System.out.print("Ingrese el segundo numero: ");
        double num2 = leerDouble();
        
        System.out.print("Ingrese el tercer numero: ");
        double num3 = leerDouble();
        
        System.out.print("Ingrese el cuarto numero: ");
        double num4 = leerDouble();
        
        double menor = num1;
        
        if (num2 < menor) {
            menor = num2;
        }
        if (num3 < menor) {
            menor = num3;
        }
        if (num4 < menor) {
            menor = num4;
        }
        
        System.out.println("El menor numero es: " + menor);
    }
    
    // 2.3 Determinar si un numero es par o impar
    public void determinarParImpar() {
        System.out.println("\n=== DETERMINAR SI UN NUMERO ES PAR O IMPAR ===");
        
        System.out.print("Ingrese un numero entero: ");
        int numero = leerEntero();
        
        if (numero % 2 == 0) {
            System.out.println("El numero " + numero + " es PAR");
        } else {
            System.out.println("El numero " + numero + " es IMPAR");
        }
    }
    
    // 2.4 Verificar si el mayor es divisible por el menor
    public void verificarDivisibilidad() {
        System.out.println("\n=== VERIFICAR SI EL MAYOR ES DIVISIBLE POR EL MENOR ===");
        
        System.out.print("Ingrese el primer numero real: ");
        double num1 = leerDouble();
        
        System.out.print("Ingrese el segundo numero real: ");
        double num2 = leerDouble();
        
        // Evitar división por cero
        while (num1 == 0 || num2 == 0) {
            System.out.println("Ninguno de los numero puede ser cero para verificar divisibilidad.");
            System.out.print("Ingrese el primer numero real: ");
            num1 = leerDouble();
            System.out.print("Ingrese el segundo numero real: ");
            num2 = leerDouble();
        }
        
        double mayor, menor;
        if (num1 > num2) {
            mayor = num1;
            menor = num2;
        } else {
            mayor = num2;
            menor = num1;
        }
        
        if (mayor % menor == 0) {
            System.out.println("El numero mayor (" + mayor + ") es divisible por el menor (" + menor + ")");
        } else {
            System.out.println("El numero mayor (" + mayor + ") NO es divisible por el menor (" + menor + ")");
        }
    }
    
    // 2.5 Determinar signo zodiacal según fecha de nacimiento
    public void determinarSignoZodiacal() {
        System.out.println("\n=== DETERMINAR SIGNO ZODIACAL SEGUN FECHA DE NACIMIENTO ===");
        
        System.out.print("Ingrese el día de nacimiento (1-31): ");
        int dia = leerEntero();
        while (dia < 1 || dia > 31) {
            System.out.print("Día invalido. Ingrese un valor entre 1 y 31: ");
            dia = leerEntero();
        }
        
        System.out.print("Ingrese el mes de nacimiento (1-12): ");
        int mes = leerEntero();
        while (mes < 1 || mes > 12) {
            System.out.print("Mes invalido. Ingrese un valor entre 1 y 12: ");
            mes = leerEntero();
        }
        
        // Validar combinaciones invalidas de día y mes
        boolean fechaValida = true;
        
        // Meses con 30 días: abril (4), junio (6), septiembre (9), noviembre (11)
        if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
            fechaValida = false;
        }
        // Febrero (mes 2)
        else if (mes == 2 && dia > 29) {
            fechaValida = false;
        }
        
        while (!fechaValida) {
            System.out.println("Fecha invalida. La combinación de día y mes no existe.");
            
            System.out.print("Ingrese el día de nacimiento (1-31): ");
            dia = leerEntero();
            while (dia < 1 || dia > 31) {
                System.out.print("Día invalido. Ingrese un valor entre 1 y 31: ");
                dia = leerEntero();
            }
            
            System.out.print("Ingrese el mes de nacimiento (1-12): ");
            mes = leerEntero();
            while (mes < 1 || mes > 12) {
                System.out.print("Mes invalido. Ingrese un valor entre 1 y 12: ");
                mes = leerEntero();
            }
            
            fechaValida = true;
            
            if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
                fechaValida = false;
            } else if (mes == 2 && dia > 29) {
                fechaValida = false;
            }
        }
        
        String signo = "";
        
        // Determinar el signo zodiacal
        if ((mes == 3 && dia >= 21) || (mes == 4 && dia <= 19)) {
            signo = "Aries";
        } else if ((mes == 4 && dia >= 20) || (mes == 5 && dia <= 20)) {
            signo = "Tauro";
        } else if ((mes == 5 && dia >= 21) || (mes == 6 && dia <= 20)) {
            signo = "Géminis";
        } else if ((mes == 6 && dia >= 21) || (mes == 7 && dia <= 22)) {
            signo = "Cáncer";
        } else if ((mes == 7 && dia >= 23) || (mes == 8 && dia <= 22)) {
            signo = "Leo";
        } else if ((mes == 8 && dia >= 23) || (mes == 9 && dia <= 22)) {
            signo = "Virgo";
        } else if ((mes == 9 && dia >= 23) || (mes == 10 && dia <= 22)) {
            signo = "Libra";
        } else if ((mes == 10 && dia >= 23) || (mes == 11 && dia <= 21)) {
            signo = "Escorpio";
        } else if ((mes == 11 && dia >= 22) || (mes == 12 && dia <= 21)) {
            signo = "Sagitario";
        } else if ((mes == 12 && dia >= 22) || (mes == 1 && dia <= 19)) {
            signo = "Capricornio";
        } else if ((mes == 1 && dia >= 20) || (mes == 2 && dia <= 18)) {
            signo = "Acuario";
        } else if ((mes == 2 && dia >= 19) || (mes == 3 && dia <= 20)) {
            signo = "Piscis";
        }
        
        System.out.println("Su signo zodiacal es: " + signo);
    }
    
    // 2.6 Comparar longitud de apellidos
    public void compararLongitudApellidos() {
        System.out.println("\n=== COMPARAR LONGITUD DE APELLIDOS ===");
        
        System.out.print("Ingrese el nombre de la primera persona: ");
        String nombre1 = leerCadena();
        
        System.out.print("Ingrese el apellido de la primera persona: ");
        String apellido1 = leerCadena();
        
        System.out.print("Ingrese el nombre de la segunda persona: ");
        String nombre2 = leerCadena();
        
        System.out.print("Ingrese el apellido de la segunda persona: ");
        String apellido2 = leerCadena();
        
        int longitud1 = apellido1.length();
        int longitud2 = apellido2.length();
        
        System.out.println("\nComparación de longitudes:");
        System.out.println(nombre1 + " " + apellido1 + ": " + longitud1 + " caracteres");
        System.out.println(nombre2 + " " + apellido2 + ": " + longitud2 + " caracteres");
        
        if (longitud1 > longitud2) {
            System.out.println("\n" + nombre1 + " " + apellido1 + " tiene el apellido más largo.");
        } else if (longitud2 > longitud1) {
            System.out.println("\n" + nombre2 + " " + apellido2 + " tiene el apellido más largo.");
        } else {
            System.out.println("\nAmbos apellidos tienen la misma longitud.");
        }
    }
    
    // 2.7 Tabla de multiplicar
    public void tablaMultiplicar() {
        System.out.println("\n=== TABLA DE MULTIPLICAR ===");
        
        System.out.print("Ingrese un número natural para ver su tabla de multiplicar: ");
        int numero = leerEntero();
        
        while (numero <= 0) {
            System.out.print("Debe ingresar un número natural (mayor que cero). Intente nuevamente: ");
            numero = leerEntero();
        }
        
        System.out.println("\nTabla de multiplicar del " + numero + ":");
        for (int i = 1; i <= 10; i++) {
            System.out.println(numero + " x " + i + " = " + (numero * i));
        }
    }
    
    // 2.8 Verificar si un numero es primo
    public void verificarNumeroPrimo() {
        System.out.println("\n=== VERIFICAR SI UN NÚMERO ES PRIMO ===");
        
        System.out.print("Ingrese un número natural: ");
        int numero = leerEntero();
        
        while (numero <= 0) {
            System.out.print("Debe ingresar un numero natural (mayor que cero). Intente nuevamente: ");
            numero = leerEntero();
        }
        
        boolean es_primo = true;
        
        if (numero == 1) {
            es_primo = false; // 1 no es primo por definicion
        } else {
            for (int i = 2; i <= Math.sqrt(numero); i++) {
                if (numero % i == 0) {
                	es_primo = false;
                    break;
                }
            }
        }
        
        if (es_primo) {
            System.out.println("El numero " + numero + " ES EL PRIMOOOOOOOOOOOOOOOOOOOO.");
        } else {
            System.out.println("El numero " + numero + " NO ES PRIMO.");
        }
    }
}
