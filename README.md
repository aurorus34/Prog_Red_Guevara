## Datos del Alumno
**Nombre:** Alvaro Ignacio Guevara Cufre 
**Email:** alvaroguevaraet32@gmail.com  
**Curso:** 6to 1ra Computación T.M

## Datos del Docente
**Nombre:** Gonzalo Nicolas Consorti  
**Email:** consor92@gmail.com

#Indice

# 📚 Guía de Ejercicios de Input/Output - Programación en Redes

## Introducción

Esta guía contiene ejercicios prácticos para aprender a manejar entrada y salida de datos en Java, utilizando los métodos estándar para lectura y escritura en consola.

---

## Sección 1: Ejercicios con Class.System (Entrada/Salida)

Utiliza **SOLO** los métodos de `Class.System` para la lectura y `PrintStream` para escritura en consola.

### 📝 Ejercicio 1.0 - Cálculo de Sueldo Bruto
> Dados el valor de una hora de trabajo y la cantidad de horas trabajadas, calcular el valor del sueldo bruto.

### 📐 Ejercicio 1.1 - Ángulos de un Triángulo
> Dados los valores de dos de los ángulos interiores de un triángulo, calcular el valor del ángulo restante.

### 🔲 Ejercicio 1.2 - Cálculo de Perímetro de un Cuadrado
> Dada la superficie de un cuadrado (en m²), calcular su perímetro.

### 🌡️ Ejercicio 1.3 - Conversión de Temperatura
> Dada una temperatura en grados Fahrenheit, convertirla a grados centígrados.

### ⏱️ Ejercicio 1.4 - Conversión de Tiempo
> Dado un tiempo en segundos, expresarlo en días, horas, minutos y segundos.

### 💰 Ejercicio 1.5 - Planes de Pago
> Dado el precio de un artículo, calcular los valores a pagar según los siguientes planes:
> 
> **Plan 1:** 100% al contado. Se hace el 10% de descuento sobre el precio publicado.
> 
> **Plan 2:** 50% al contado y el resto en 2 cuotas iguales. El precio publicado se incrementa en un 10%.
> 
> **Plan 3:** 25% al contado y el resto en 5 cuotas iguales. El precio publicado se incrementa en un 15%.
> 
> **Plan 4:** Totalmente financiado en 8 cuotas. El 60% se reparte en partes iguales en las primeras 4 cuotas y el resto se reparte en partes iguales en las últimas 4 cuotas. El precio publicado se incrementa en un 25%.

### ♈ Ejercicio 1.6 - Signo Zodiacal y Mes de Nacimiento
> Dado el signo zodiacal del usuario, determinar su mes de nacimiento aproximado.

---

## Sección 2: Ejercicios con Class.Reader

Utiliza **SOLO** los métodos de `Class.Reader` para la lectura y `PrintStream` para escritura en consola.

### 📋 Ejercicio 2.1 - Ordenamiento Alfabético
> Dados tres apellidos, mostrarlos ordenados alfabéticamente.

### 🔢 Ejercicio 2.2 - Número Menor
> Dados cuatro números reales, determinar cuál es el menor.

### 🧮 Ejercicio 2.3 - Par o Impar
> Dado un número, determinar si es par o impar.

### ➗ Ejercicio 2.4 - Divisibilidad
> Dados dos números reales, determinar si el mayor es divisible por el menor.

### 🌟 Ejercicio 2.5 - Signo Zodiacal
> Dada la fecha de nacimiento de una persona, determinar su signo del zodíaco.

### 📏 Ejercicio 2.6 - Apellido más Largo
> Dado el nombre y apellido de 2 personas, determinar cuál de los 2 tiene el apellido más largo.

### ✖️ Ejercicio 2.7 - Tabla de Multiplicar
> Dado un entero N natural, mostrar su tabla de multiplicar.

### 🔍 Ejercicio 2.8 - Número Primo
> Dado un número natural, determinar si es primo o no.

---

## Desafíos técnicos encontrados

Durante el desarrollo de este trabajo práctico, me enfrenté a varios desafios, que por mas absurdas que sean, que considero importante documentar para reflexionar sobre el proceso de aprendizaje.

### Problemas con la validación de entrada

Uno de los principales desafíos fue implementar una validación de los datos ingresados por el usuario. Requeri verificar y validar todos los datos para evitar errores, lo que implicó:

- **Manejo de excepciones**: Tuve que implementar bloques try-catch en cada método de entrada para capturar posibles errores de formato o tipo.
- **Bucles de validación**: Desarrollé estructuras repetitivas para solicitar nuevamente los datos cuando estos no cumplían con los requisitos establecidos.
- **Casos especiales**: Para ejercicios como el cálculo del ángulo restante de un triángulo, me fue necesario implementar validaciones específicas para asegurar que la suma de los ángulos no excediera los 180 grados.

### Dificultades con la lectura de datos usando System

La restricción de utilizar únicamente los métodos de la clase System para la lectura de datos en la primera sección de ejercicios no resultó particularmente desafiante, pero alargo bastante el codigo y por ende tuve mayor probabilidaeds de mandarme cagada tras cagada:

- **Limitaciones de System.in**: Las clases más modernas como Scanner o BufferedReader, System.in es más básico y requiere mayor cantidad de codigo para obtener y procesar los datos.
- **Conversión de bytes a tipos primitivos**: Tuve que implementar métodos auxiliares para convertir los bytes leídos a través de BufferedInputStream en tipos utilizables como String, double o int.
- **Manejo del buffer**: La lectura de datos consecutivos me dio complicaciones por el comportamiento del buffer de entrada, lo que requirió una cuidadosa implementación para evitar lecturas incompletas o incorrectas.

- **Eliminacion del buffer reader**: Tuve que eliminar el buffer reader >:(
