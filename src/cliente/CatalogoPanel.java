package cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class CatalogoPanel extends JPanel {
    private DefaultTableModel modelo;
    private JTable tabla;
    private ClienteSocket clienteSocket;

    public CatalogoPanel(JFrame frame, ClienteSocket clienteSocket) {
        this.clienteSocket = clienteSocket;
        setLayout(new BorderLayout());

        JPanel filtrosPanel = new JPanel(new GridLayout(2, 4));
        JTextField tituloField = new JTextField();
        JTextField autorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField editorialField = new JTextField();
        filtrosPanel.add(new JLabel("Título"));
        filtrosPanel.add(tituloField);
        filtrosPanel.add(new JLabel("Autor"));
        filtrosPanel.add(autorField);
        filtrosPanel.add(new JLabel("ISBN"));
        filtrosPanel.add(isbnField);
        filtrosPanel.add(new JLabel("Editorial"));
        filtrosPanel.add(editorialField);

        JButton filtrarBtn = new JButton("Buscar");
        filtrosPanel.add(filtrarBtn);

        add(filtrosPanel, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"ID", "Título", "Autor", "ISBN", "Editorial"}, 0);
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        tabla.setComponentPopupMenu(crearMenuContextual());

        filtrarBtn.addActionListener(e -> cargarCatalogo(tituloField.getText(), autorField.getText(), isbnField.getText(), editorialField.getText()));

        cargarCatalogo("", "", "", "");
    }

    private void cargarCatalogo(String tituloFiltro, String autorFiltro, String isbnFiltro, String editorialFiltro) {
        try {
            modelo.setRowCount(0);
            String respuesta = clienteSocket.enviar("GET_BOOKS");
            if (respuesta == null || respuesta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se pudo obtener el catálogo del servidor.");
                return;
            }
            String[] libros = respuesta.split("\\|");
            for (int i = 1; i < libros.length; i++) {
                String[] partes = libros[i].split(";");
                if (partes.length == 5) {
                    String id = partes[0];
                    String titulo = partes[1];
                    String autor = partes[2];
                    String isbn = partes[3];
                    String editorial = partes[4];

                    // Aplicar filtros (ignorar si filtro está vacío)
                    if ((!tituloFiltro.isEmpty() && !titulo.toLowerCase().contains(tituloFiltro.toLowerCase())) ||
                            (!autorFiltro.isEmpty() && !autor.toLowerCase().contains(autorFiltro.toLowerCase())) ||
                            (!isbnFiltro.isEmpty() && !isbn.contains(isbnFiltro)) ||
                            (!editorialFiltro.isEmpty() && !editorial.toLowerCase().contains(editorialFiltro.toLowerCase()))) {
                        continue; // no cumple filtro, saltar libro
                    }

                    modelo.addRow(new Object[]{id, titulo, autor, isbn, editorial});
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private JPopupMenu crearMenuContextual() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem verPrologo = new JMenuItem("Ver prólogo");
        JMenuItem descargarLibro = new JMenuItem("Descargar libro");

        verPrologo.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String id = (String) tabla.getValueAt(fila, 0);
                try {
                    String res = clienteSocket.enviar("GET_PROLOGUE|" + id);
                    if (res.startsWith("OK|")) {
                        JTextArea texto = new JTextArea(res.substring(3));
                        texto.setEditable(false);
                        JOptionPane.showMessageDialog(this, new JScrollPane(texto), "Prólogo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, res);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        descargarLibro.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String id = (String) tabla.getValueAt(fila, 0);
                try {
                    String res = clienteSocket.enviar("DOWNLOAD_BOOK|" + id);
                    if (res.startsWith("OK|")) {
                        JFileChooser chooser = new JFileChooser();
                        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(chooser.getSelectedFile()))) {
                                writer.write(res.substring(3));
                                JOptionPane.showMessageDialog(this, "Libro guardado.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, res);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        menu.add(verPrologo);
        menu.add(descargarLibro);
        return menu;
    }
}
