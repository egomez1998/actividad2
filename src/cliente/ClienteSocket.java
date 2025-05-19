package cliente;

import java.io.*;
import java.net.Socket;

public class ClienteSocket {
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;

    public ClienteSocket(String host, int puerto) throws IOException {
        socket = new Socket(host, puerto);
        salida = new PrintWriter(socket.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String enviar(String mensaje) throws IOException {
        salida.println(mensaje);
        return entrada.readLine();
    }

    public void cerrar() throws IOException {
        socket.close();
    }
}
