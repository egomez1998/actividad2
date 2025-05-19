package servidor;

import java.io.*;
import java.net.Socket;

public class ClienteHandler implements Runnable {

    private Socket socket;

    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("Mensaje recibido: " + mensaje);
                String respuesta = procesarMensaje(mensaje);
                salida.println(respuesta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String procesarMensaje(String mensaje) {
        String[] partes = mensaje.split("\\|");
        String comando = partes[0];

        switch (comando) {
            case "LOGIN":
                return GestorUsuarios.login(partes[1], partes[2]);
            case "REGISTER":
                return GestorUsuarios.registrar(partes[1], partes[2]);
            case "GET_BOOKS":
                return GestorLibros.obtenerCatalogo();
            case "GET_PROLOGUE":
                return GestorLibros.obtenerPrologo(partes[1]);
            case "DOWNLOAD_BOOK":
                return GestorLibros.obtenerLibroCompleto(partes[1]);
            default:
                return "ERROR|Comando no reconocido";
        }
    }
}
