/**
 * @author victor
 */
package Ejercicio1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ejercicio1v2 {
	/**
	 * Imprime el contenido del directorio f
	 * 
	 * @param f
	 *            Fichero del que se desea imprimir el contenido
	 * @throws IOException
	 */
	public static void imprimeDirectorio(File f) throws IOException {
		int cont = 0;
		if (f.exists()) {
			if (f.isDirectory()) {
				System.out.println("Lista de ficheros y directorios del directorio: " + f.getCanonicalPath());
				System.out.println("---------------------------------------------------");
				System.out.println(cont + ".- " + "Directorio padre ");
				for (File e : f.listFiles()) {
					// Aumentamos el contador para imprimirlo junto con el nombre de archivo o
					// directorio
					cont++;
					if (e.isFile())
						System.out.println(cont + ".- " + e.getName() + " " + e.length());
					if (e.isDirectory())
						System.out.println(cont + ".- " + e.getName() + " <Directorio>");
				}
			} else {
				System.out.println("No es un directorio");
			}
		} else {
			System.out.println("No existe el directorio");
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Introduce un directorio:");
		String ent = new BufferedReader(new InputStreamReader(System.in)).readLine();
		File f = new File(ent);
		imprimeDirectorio(f);
	}
}