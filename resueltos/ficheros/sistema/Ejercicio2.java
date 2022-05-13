import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Locale;

public class Ejercicio2 {
	/**
	 * Devuelve los flags del archivo al estilo linux
	 * @param f
	 * @return los flags
	 */
	static String getFlags(File f) {
		/*String flags = "";
		if (f.isFile()) {
            flags += "-";
        }
        if (f.isDirectory()) {
            flags += "d";
        }
        if (f.canRead())
        	flags += "r";
        else
        	flags += "-";
        
        if (f.canWrite())
        	flags += "w";
        else
        	flags += "-";
        
        if (f.canExecute())
        	flags += "x";
        else
        	flags += "-";
        	
        	return flags;
        */
        
        return (f.isFile()? "-" : "") 
        		+ (f.isDirectory()? "d" : "") 
        		+ (f.canRead()? "r" : "-") 
        		+ (f.canWrite()? "w" : "-")
        		+ (f.canExecute()? "x" : "-");
        
	}
	/**
	 * Devuelve un String con los datos del Fichero
	 * @param cont
	 * @param f
	 * @return la cadena formateadas
	 */
	static String imprimeLinea(int cont, File f) {
		DateFormat formatter;
		formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault());
    	return (cont + ".- \t" + getFlags(f) + "\t" +  String.format("%-15d", f.length()) + formatter.format(f.lastModified()) + "\t" + f.getName());
		
	}
	/**
	 * Imprime el contenido del directorio f. 
	 * 
	 * @param f
	 *            Directorio del que se desea imprimir el contenido
	 * @throws IOException
	 */
	public static void imprimeDirectorio(File f) throws IOException {
		int cont = 0;
		
		System.out.println("Lista de ficheros y directorios del directorio: " + f.getCanonicalPath());
		System.out.println("---------------------------------------------------");
		System.out.println(cont + ".- " + "Directorio padre ");
		for (File e : f.listFiles()) {
			// Aumentamos el contador para imprimirlo junto con el nombre de archivo o
			// directorio
			cont++;
			System.out.println(imprimeLinea(cont, e));
		}


	}

	/**
	 * Da la opción al usuario de seleccionar una opción. Comprueba que esta opción
	 * está dentro del límite -1 .. número de ficheros
	 * 
	 * @param numberOfFiles
	 *            número de ficheros
	 * @return la opción seleccionada por el usuario
	 * @throws IOException
	 */
	public static int gestionMenu(int numberOfFiles) throws IOException {
		boolean ok;

		int opcion = -1;
		do {
			// Este bucle controla que el usuario introduce un número
			ok = true;
			System.out.println("Introduce una opción (-1 para salir): ");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			try {
				opcion = Integer.parseInt(in.readLine()); // Lee una cadena, pero tenemos que transformarla a entero
				// Control de opción correcta
				if ((opcion < -1) || (opcion > numberOfFiles)) {
					ok = false;
					System.out.printf("Opción incorrecta. Opciones válidas entre -1 y %d%n", numberOfFiles);
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Opción incorrecta. Sólo números enteros.");
				ok = false;
			}
		} while (!ok);

		return opcion;
	}

	/**
	 * A partir de un directorio y una opción, devuelve el próximo directorio a
	 * procesar
	 * 
	 * @param currentFile
	 *            directorio actual
	 * @param numDir
	 *            opción a procesar
	 * @return el directorio a procesar
	 */
	public static File obtenerDirectorio(File currentFile, int numDir) {
		if (currentFile == null) {
			// La primera vez, empezamos por root
			return File.listRoots()[0];
		} else if (0 == numDir) {
			// Si queremos ir al padre
			// Ahora ya controlamos que tiene padre y, si lo tiene, que se puede leer
			if (currentFile.getParentFile() != null && currentFile.getParentFile().canRead()) {
				return currentFile.getParentFile();
			}
		} else {
			// En otro caso, queremos ir a un directorio hijo
			// Y comprobamos que es un directorio y que se puede leer
			if (currentFile.listFiles()[numDir - 1].isDirectory() && currentFile.listFiles()[numDir - 1].canRead()) {
				return currentFile.listFiles()[numDir - 1];
			}
		}
		// Si llega hasta aquí, devolvemos el mismo directorio
		return currentFile;
	}
 	public static void main(String[] args) throws IOException {
		int opcion = -2;
		File f = null;
		do {
			f = obtenerDirectorio(f, opcion);
			imprimeDirectorio(f);
			opcion = gestionMenu(f.listFiles().length);
		} while (-1 != opcion);

		System.out.println("Fin del programa");
	}
}