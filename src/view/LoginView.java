package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    public JTextField txtCorreo; // Campo para ingresar el correo
    public JPasswordField txtContraseña; // Campo para ingresar la contraseña
    public JButton btnLogin, btnRegister; // Botones para ingresar o registrar

    public LoginView() {
        setTitle("Login - Sistema de Orquídeas");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("Iniciar Sesión", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panelFormulario.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo);

        panelFormulario.add(new JLabel("Contraseña:"));
        txtContraseña = new JPasswordField();
        panelFormulario.add(txtContraseña);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnLogin = new JButton("Ingresar");
        btnLogin.setPreferredSize(new Dimension(90, 25));
        btnRegister = new JButton("Registrar");
        btnRegister.setPreferredSize(new Dimension(90, 25));

        panelBotones.add(btnLogin);
        panelBotones.add(btnRegister);

        // Agregar componentes a la vista
        add(lblTitulo, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
}








