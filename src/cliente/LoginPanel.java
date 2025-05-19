package cliente;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginPanel extends JPanel {

    public LoginPanel(JFrame frame, ClienteSocket clienteSocket) {
        setLayout(new GridLayout(4, 1, 10, 10));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Registrar");

        add(new JLabel("Usuario:"));
        add(userField);
        add(new JLabel("Contraseña:"));
        add(passField);
        add(loginBtn);
        add(registerBtn);

        loginBtn.addActionListener(e -> {
            try {
                String user = userField.getText();
                String pass = new String(passField.getPassword());
                String res = clienteSocket.enviar("LOGIN|" + user + "|" + pass);
                if (res.startsWith("OK")) {
                    frame.setContentPane(new CatalogoPanel(frame, clienteSocket));
                    frame.revalidate();
                } else {
                    JOptionPane.showMessageDialog(this, res);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al intentar conectar.");
            }
        });

        registerBtn.addActionListener(e -> {
            // Aquí abrimos una nueva ventana de registro
            new RegisterWindow(frame, clienteSocket);
        });
    }
}
