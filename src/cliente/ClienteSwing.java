package cliente;

import javax.swing.*;
import java.io.IOException;

public class ClienteSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ClienteSocket clienteSocket = new ClienteSocket("localhost", 12345);
                JFrame frame = new JFrame("Cliente Librería");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 300);
                frame.setLocationRelativeTo(null);
                frame.setContentPane(new LoginPanel(frame, clienteSocket));
                frame.setVisible(true);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "No se pudo conectar al servidor.");
                e.printStackTrace();
            }
        });
    }
}
