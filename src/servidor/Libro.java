package servidor;

public class Libro {
    private String id;
    private String titulo;
    private String autor;
    private String prologo;
    private String contenido;
    private String isbn;
    private String editorial;

    public Libro(String id, String titulo, String autor, String prologo, String contenido, String isbn, String editorial) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.prologo = prologo;
        this.contenido = contenido;
        this.isbn = isbn;
        this.editorial = editorial;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getPrologo() { return prologo; }
    public String getContenido() { return contenido; }
    public String getIsbn() { return isbn; }
    public String getEditorial() { return editorial; }

    @Override
    public String toString() {
        return id + " - " + titulo + " (por " + autor + ")";
    }
}
