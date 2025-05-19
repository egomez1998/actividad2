package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import servidor.GestorLibros;
public class GestorLibrosTest {

    @Test
    void testObtenerPrologoExistente() {
        GestorLibros gestor = new GestorLibros();
        String resultado = gestor.obtenerPrologo("Orgullo y Prejuicio");
        assertEquals("OK", resultado);
    }

    @Test
    void testObtenerPrologoNoExistente() {
        GestorLibros gestor = new GestorLibros();
        String resultado = gestor.obtenerPrologo("Libro Inexistente");
        assertEquals("ERROR|Prólogo no encontrado", resultado);
    }

    @Test
    void testObtenerLibroCompletoExistente() {
        GestorLibros gestor = new GestorLibros();
        String resultado = gestor.obtenerLibroCompleto("Orgullo y Prejuicio");
        assertEquals("OK|Contenido completo de Orgullo y Prejuicio", resultado);
    }

    @Test
    void testObtenerLibroCompletoNoExistente() {
        GestorLibros gestor = new GestorLibros();
        String resultado = gestor.obtenerLibroCompleto("Libro Inexistente");
        assertEquals("ERROR|Libro no encontrado", resultado);
    }
}
