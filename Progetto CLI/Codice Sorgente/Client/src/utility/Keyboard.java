package utility;

import java.io.*;
import java.util.*;

/**
 * Classe per gestione degli input da tastiera.
 */

public class Keyboard {

	/**
	 * Booleano che serve a stampare gli errori.
	 */
	private static boolean printErrors = true;

	/**
	 * Intero che conta gli errori.
	 */
	private static int errorCount = 0;

	/**
	 * Metodo che restituisce il numero di errori.
	 * @return Intero che indica numero di errori.
	 */
	public static int getErrorCount() {
		return errorCount;
	}

	/**
	 * Metodo che resetta il numero di errori a zero.
	 * @param count - Intero che indica la conta degli errori.
	 */
	public static void resetErrorCount(int count) {
		errorCount = 0;
	}

	/**
	 * Metodo che restituisce un valore booleano che indica
	 * se gli errori di input sono attualmente stampati sullo standard output.
	 * 
	 * @return Booleano che è posto a true se gli errori sono
	 * stampati sullo standard output
	 */
	public static boolean getPrintErrors() {
		return printErrors;
	}


	/**
	 * Imposta un valore booleano che indica se devono esserci errori di input
	 * stampati sul output standard.
	 * @param flag - Booleano di riferimento per gli errori di input
	 */
	public static void setPrintErrors(boolean flag) {
		printErrors = flag;
	}

	/**
	 * Metodo che incrementa il counter di errori e stampa
	 * il messaggio di errore relativo.
	 * @param str - Stringa che rappresenta l'errore
	 */
	private static void error(String str) {
		errorCount++;
		if (printErrors) {
			System.out.println(str);
		}
	}


	/**
	 * Stringa indicante il token corrente.
	 */
	private static String current_token = null;

	/**
	 * Token che rappresenta una stringa.
	 */
	private static StringTokenizer reader;

	/**
	 * Lettore del Buffer in input.
	 */
	private static BufferedReader in = new BufferedReader(
			new InputStreamReader(System.in));


	/**
	 * Metodo che Ottiene il token di input successivo
	 * presupponendo che possa essere su  linee di input successive.
	 * @return Booleano che assume valore di vero o falso.
	 */
	private static String getNextToken() {
		return getNextToken(true);
	}


	/**
	 * Metodo che ottiene il prossimo token di input, che potrebbe essere già letto.
	 * @param skip - Booleano che indica se il token è gia stato letto
	 * @return Stringa contente il token letto
	 */
	private static String getNextToken(boolean skip) {
		String token;

		if (current_token == null) {
			token = getNextInputToken(skip);
		} else {
			token = current_token;
			current_token = null;
		}

		return token;
	}


	/**
	 * Metodo che ottiene il token successivo dall'input, che
	 * potrebbe provenire dalla linea corrente o seguente.
	 * @param skip - Booleano che determina se saltare o meno il token
	 * @return Stringa contenente il token letto.
	 */
	private static String getNextInputToken(boolean skip) {
		final String delimiters = " \t\n\r\f";
		String token = null;

		try {
			if (reader == null) {
				reader = new StringTokenizer(in.readLine(), delimiters, true);
			}

			while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
				while (!reader.hasMoreTokens()) {
					reader = new StringTokenizer(in.readLine(), delimiters,
							true);
				}

				token = reader.nextToken();
			}
		} catch (Exception exception) {
			token = null;
		}

		return token;
	}

	/**
	 * Metodo che indica l'assenza di token da leggere sulla linea di input.
	 * @return Booleano che indica se non ci sono più token da leggere.
	 */
	public static boolean endOfLine() {
		return !reader.hasMoreTokens();
	}

	/**
	 * Metodo che legge una stringa.
	 * @return Stringa letta da tastiera.
	 */
	public static String readString() {
		String str;

		try {
			str = getNextToken(false);
			while (!endOfLine()) {
				str = str + getNextToken(false);
			}
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			str = null;
		}
		return str;
	}

	/**
	 * Metodo che legge una parola (sottostringa).
	 * @return Stringa che indica la parola letta da tastiera.
	 */
	public static String readWord() {
		String token;
		try {
			token = getNextToken();
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			token = null;
		}
		return token;
	}

	/**
	 * Metodo che legge una booleano.
	 * @return Booleano letto da tastiera.
	 */
	public static boolean readBoolean() {
		String token = getNextToken();
		boolean bool;
		try {
			if (token.toLowerCase().equals("true")) {
				bool = true;
			} else if (token.toLowerCase().equals("false")) {
				bool = false;
			} else {
				error("Error reading boolean data, false value returned.");
				bool = false;
			}
		} catch (Exception exception) {
			error("Error reading boolean data, false value returned.");
			bool = false;
		}
		return bool;
	}


	/**
	 * Metodo che legge un carattere.
	 * @return Char letto da tastiera.
	 */
	public static char readChar() {
		String token = getNextToken(false);
		char value;
		try {
			if (token.length() > 1) {
				current_token = token.substring(1, token.length());
			} else {
				current_token = null;
			}
			value = token.charAt(0);
		} catch (Exception exception) {
			error("Error reading char data, MIN_VALUE value returned.");
			value = Character.MIN_VALUE;
		}

		return value;
	}

	/**
	 * Metodo che legge un intero.
	 * @return Intero letto da tastiera.
	 */
	public static int readInt() {
		String token = getNextToken();
		int value;
		try {
			value = Integer.parseInt(token);
		} catch (Exception exception) {
			error("Error reading int data, MIN_VALUE value returned.");
			value = Integer.MIN_VALUE;
		}
		return value;
	}

	/**
	 * Metodo che legge un intero a 64 bit.
	 * @return Intero letto da tastiera.
	 */
	public static long readLong() {
		String token = getNextToken();
		long value;
		try {
			value = Long.parseLong(token);
		} catch (Exception exception) {
			error("Error reading long data, MIN_VALUE value returned.");
			value = Long.MIN_VALUE;
		}
		return value;
	}

	/**
	 * Metodo che legge un decimale.
	 * @return Decimale letto da tastiera.
	 */
	public static float readFloat() {
		String token = getNextToken();
		float value;
		try {
			value = (new Float(token)).floatValue();
		} catch (Exception exception) {
			error("Error reading float data, NaN value returned.");
			value = Float.NaN;
		}
		return value;
	}


	/**
	 * Metodo che legge un decimale a 64 bit.
	 * @return Double letto da tastiera.
	 */
	public static double readDouble() {
		String token = getNextToken();
		double value;
		try {
			value = (new Double(token)).doubleValue();
		} catch (Exception exception) {
			error("Error reading double data, NaN value returned.");
			value = Double.NaN;
		}
		return value;
	}
}
