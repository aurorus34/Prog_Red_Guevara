package classReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {
	
	public static void main(String[] args) {
		
		InputStreamReader is = new InputStreamReader( System.in );
		BufferedReader buff = new BufferedReader( is );
		
		PrintStream ps = new PrintStream( System.out );
		
		try {
			
			String texto = buff.readLine();
			ps.println( texto );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
