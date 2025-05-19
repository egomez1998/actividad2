package servidor;

import java.util.*;
import java.io.*;

public class GestorLibros {

    private static Map<String, Libro> catalogo = new HashMap<>();
    private static Map<String, Integer> descargas = new HashMap<>();

    // Carga libros desde archivo al inicializar (puedes llamar esto manualmente también)
    static {
        cargarLibrosDesdeArchivo("libros.txt");
    }

    // Reinicia el catálogo (útil para pruebas)
    public static void reiniciarCatalogo() {
        catalogo.clear();
        descargas.clear();
    }

    // Carga libros desde archivo
    public static void cargarLibrosDesdeArchivo(String archivo) {
        reiniciarCatalogo();
        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 7) {
                    Libro libro = new Libro(partes[0], partes[1], partes[2], partes[3], partes[4], partes[5], partes[6]);
                    catalogo.put(libro.getId(), libro);
                    descargas.put(libro.getId(), 0);
                    contador++;
                } else {
                    System.err.println("Línea mal formada: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando libros: " + e.getMessage());
        }
        System.out.println("Libros cargados: " + contador);
    }

    // Carga libros desde una lista de líneas (para pruebas unitarias)
    public static void cargarLibrosDesdeLineas(List<String> lineas) {
        reiniciarCatalogo();
        for (String linea : lineas) {
            String[] partes = linea.split("\\|");
            if (partes.length == 7) {
                Libro libro = new Libro(partes[0], partes[1], partes[2], partes[3], partes[4], partes[5], partes[6]);
                catalogo.put(libro.getId(), libro);
                descargas.put(libro.getId(), 0);
            } else {
                System.err.println("Línea mal formada: " + linea);
            }
        }
    }

    // Obtiene la lista de libros en formato para mostrar (ID;Título;Autor;ISBN;Editorial)
    public static String obtenerCatalogo() {
        StringBuilder sb = new StringBuilder("OK");
        for (Libro libro : catalogo.values()) {
            sb.append("|").append(libro.getId()).append(";")
                    .append(libro.getTitulo()).append(";")
                    .append(libro.getAutor()).append(";")
                    .append(libro.getIsbn()).append(";")
                    .append(libro.getEditorial());
        }
        return sb.toString();
    }

    // Obtiene el prólogo de un libro
    public static String obtenerPrologo(String idLibro) {
        Libro libro = catalogo.get(idLibro);
        if (libro != null) {
            return "OK|" + libro.getPrologo();
        }
        return "ERROR|Libro no encontrado";
    }

    // Obtiene el contenido completo del libro y suma 1 a contador de descargas
    public static String obtenerLibroCompleto(String idLibro) {
        Libro libro = catalogo.get(idLibro);
        if (libro != null) {
            descargas.put(idLibro, descargas.getOrDefault(idLibro, 0) + 1);
            return "OK|" + libro.getContenido();
        }
        return "ERROR|Libro no encontrado";
    }
}
