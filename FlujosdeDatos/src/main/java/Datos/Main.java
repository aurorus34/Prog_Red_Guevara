package Datos;

import java.io.IOException;
import java.io.PrintStream;


public class Main {

	public static void main(String[] args) {

		
		//SubClase: OUT
		System.out.println("Primer dato a mostrar.");
		System.out.print("Hola.");
		System.out.print("Chau.");
		System.out.println("Segundo dato a mostrar");
		System.out.write(77);
		System.out.write(10);
		System.out.write(13);
		System.out.flush();
		
		byte[] aray = {60 , 45 , 100 , 59 , 77, -41 , 78} ;
		try {
			System.out.write(aray);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Proxima subClase: ERR
		
		System.err.println("ESTE MENSAJE ES UN ERROR");
		
		//SubClase: IN
		
//		int dato = -1;
//		//programacion
//		try {
//			dato = System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println();
//		
//		System.out.println( "El dato en fromato de byte:" + dato );
//		System.out.write(dato);
//		System.out.write(10);
//		System.out.write(13);
//		System.out.flush();
//		System.out.println("El dato en fromato de texto:" + (char)dato);
		
//		int texto = -1;
//		String textoFinal = "";
//		try {
//			while(   (texto = System.in.read()) != '\n'  ) 
//			{
//				System.out.print( (char)texto );
//				textoFinal = textoFinal + (char)texto;
//			}
//			System.out.println(textoFinal);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		
		//Clase PrintStream
		PrintStream obj;
		
		obj = new PrintStream( System.out );
		
		obj.println("Este mensaje se escribe atravez de la clase printStream.");
	}
}
