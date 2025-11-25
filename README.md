# Datos del Alumno

**Nombre:** Maximiliano Passo Koziura  
**Email:** maximilianokoziura.et32@gmail.com  
**Curso:** 6to 1ra Computación T.M  

# Datos del Docente

**Nombre:** Gonzalo Nicolas Consorti  
**Email:** consor92@gmail.com  

---

# 📦 Sistema de Gestión de Inventario - Java

## 📋 Introducción

Este trabajo práctico consiste en desarrollar una aplicación de consola en Java para la gestión básica de un inventario de productos. A través de un menú interactivo, el usuario podrá cargar productos, listarlos, modificarlos o eliminarlos, con persistencia de datos en archivos de texto plano.

---

## #Indice

### 🔧 Funcionalidades requeridas

- Menú principal con las opciones:
  - Agregar producto
  - Mostrar productos
  - Eliminar producto
  - Editar producto
  - Salir
- Lectura de texto desde consola y conversión segura a tipos numéricos.
- Validación para identificar si un texto es:
  - No numérico
  - Entero
  - Número con coma
- Métodos auxiliares para:
  - Convertir texto a `int` o `float`
  - Crear archivo de inventario (`Inventario.dat`)
  - Agregar productos al archivo con formato delimitado por `;`
  - Leer y mostrar datos desde el archivo de manera ordenada
  - Editar datos existentes
  - Eliminar un producto

---

## 🛠️ Estructura del archivo

Cada producto se guarda en una línea de la siguiente forma:

Ejemplo: consor.log(92);0.0;0.0;1


---

## 🖥️ Interfaz en Consola

Se utilizó impresión prolija mediante tabulaciones y saltos de línea para mejorar la legibilidad. Además, se aplicó color a los mensajes utilizando códigos ANSI para destacar títulos, advertencias, errores y confirmaciones.

---

## 🚧 Desafíos técnicos encontrados

### 🧪 Validación y conversión de entrada

Uno de los principales retos fue desarrollar un método robusto para validar si una entrada es numérica, entera o con decimales. Esto implicó:

- Implementación de expresiones regulares para detección de enteros y flotantes.
- Manejo de excepciones con `try-catch` para evitar caídas por entrada inválida.
- Separación en métodos reutilizables para convertir `String` a `int` o `float`.

### 📂 Lectura y escritura de archivos

La manipulación del archivo `Inventario.dat` fue otro foco importante:

- **Creación**: Verificación previa de existencia para no sobreescribir datos.
- **Lectura ordenada**: Separación por campos y presentación con formato de tabla.
- **Edición**: Reescritura total del archivo tras detectar y modificar la línea correspondiente.
- **Eliminación**: Filtrado y sobrescritura del archivo omitiendo el producto seleccionado.

### 💢 Manejo del menú infinito

Para mantener el menú activo y permitir múltiples operaciones sin cerrar el programa, se utilizó un bucle `while(true)` con `switch-case` y validación de entrada. Se prestó especial atención a:

- Entrada inválida de opciones
- Flujo de retorno a menú tras completar una acción
- Limpieza visual entre cada interacción

---

## 🎨 Colores en la consola

Se aplicaron códigos ANSI para dar color a las salidas:

- `\u001B[32m` para confirmaciones (verde)
- `\u001B[31m` para errores o advertencias (rojo)
- `\u001B[34m` para títulos o encabezados (azul)
- `\u001B[0m` para resetear el color

Esto contribuyó a una experiencia más agradable y organizada en la terminal.

---

## 📚 Conclusión

Este proyecto me permitió reforzar conceptos fundamentales como:

- Entrada y validación de datos
- Manipulación de archivos
- Programación modular con métodos reutilizables
- Mejora de la experiencia en consola a través de colores y formato

Más allá de los errores y complicaciones iniciales, la práctica constante y la organización del código me permitieron cumplir con todos los objetivos propuestos.

---


