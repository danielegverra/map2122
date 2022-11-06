package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe per l'avvio del Server capace di comunicare contemporaneamente con più client.
 */
public class MultiServer {


    /**
     * Intero che rappresenta la porta di default per la comunicazione col Client.
     */
    private int PORT = 2025;

    /**
     * Costruttore di MultiServer per inizializzare la porta ed invocare run().
     *
     * @param port - Porta da inizializzare.
     */
    public MultiServer(int port) throws IOException {
        PORT = port;
        run();
    }

    /**
     * Metodo per l'avvio del Server. Il Server rimane in attesa delle richieste dei diversi Client.
     *
     * @throws IOException
     */
    private void run() throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Avviato il server.");

        try {
            while (true) {
                System.out.println("Attendendo client.");
                Socket socket = serverSocket.accept();
                System.out.println("Gestendo client.");
                try {
                    new ServerOneClient(socket);
                } catch (IOException exc) {
                    socket.close();
                }
            }
        } finally {
            serverSocket.close();
        }

    }

    /**
     * Main per l'avvio del Server.
     *
     * @param args - I parametri del main.
     */
    public static void main(String[] args) {

		try {
			MultiServer server = new MultiServer(2025);
		} catch (IOException exc) {
			System.out.println(exc.getMessage());
		}

	}

}
