package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorMain {

    public static void main(String[] args) {
        int puerto = 12345;
        ExecutorService pool = Executors.newFixedThreadPool(10); // Soporta hasta 10 clientes simultáneos

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor iniciado en el puerto " + puerto);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket.getInetAddress());
                pool.execute(new ClienteHandler(clienteSocket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
