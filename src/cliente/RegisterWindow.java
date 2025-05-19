package cliente;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class RegisterWindow extends JFrame {

    public RegisterWindow(JFrame parent, ClienteSocket clienteSocket) {
        setTitle("Registro de usuario");
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 10, 10));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JPasswordField repeatField = new JPasswordField();

        JButton registerBtn = new JButton("Registrar");

        add(new JLabel("Nombre de usuario:"));
        add(userField);
        add(new JLabel("Contraseña:"));
        add(passField);
        add(new JLabel("Repetir contraseña:"));
        add(repeatField);
        add(new JLabel());
        add(registerBtn);

        registerBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            String repeat = new String(repeatField.getPassword());

            if (!pass.equals(repeat)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
                return;
            }

            try {
                String res = clienteSocket.enviar("REGISTER|" + user + "|" + pass);
                JOptionPane.showMessageDialog(this, res);
                if (res.startsWith("OK")) {
                    dispose(); // Cierra la ventana si se registra correctamente
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al registrar usuario.");
            }
        });

        setVisible(true);
    }
}
