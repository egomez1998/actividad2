package servidor;

import java.io.*;
import java.util.*;

public class GestorUsuarios {
    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private static Map<String, String> usuarios = new HashMap<>();

    static {
        cargarUsuarios();
    }

    private static void cargarUsuarios() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 2) {
                    usuarios.put(partes[0], partes[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de usuarios, se creará uno nuevo si es necesario.");
        }
    }

    private static void guardarUsuarios() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_USUARIOS))) {
            for (Map.Entry<String, String> entry : usuarios.entrySet()) {
                pw.println(entry.getKey() + "|" + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    public static String login(String usuario, String password) {
        if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(password)) {
            return "OK|Login exitoso";
        }
        return "ERROR|Usuario o contraseña incorrectos";
    }

    public static String registrar(String usuario, String password) {
        if (usuarios.containsKey(usuario)) {
            return "ERROR|Usuario ya existe";
        }
        if (password.length() < 6) {
            return "ERROR|Password demasiado corto";
        }
        usuarios.put(usuario, password);
        guardarUsuarios(); // Guardamos los cambios
        return "OK|Usuario registrado";
    }
}
