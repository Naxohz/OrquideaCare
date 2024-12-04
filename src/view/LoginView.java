package view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class LoginView extends JFrame {
    public JTextField txtCorreo; // Campo para ingresar el correo
    public JPasswordField txtContraseña; // Campo para ingresar la contraseña
    public JButton btnLogin, btnRegister; // Botones para ingresar o registrar

    public LoginView() {
        // Configuración de la ventana principal
        setUndecorated(true); // Quitar bordes de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 590); // Se ajusta el tamaño de la ventana (panel principal más pequeño)
        setLocationRelativeTo(null); // Centrar la ventana

        // Panel principal redondeado (incluye fondo verde oscuro)
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 51, 0)); // Fondo verde oscuro
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50); // Bordes redondeados
            }
        };
        panelFondo.setLayout(null);
        panelFondo.setOpaque(false);

        // Panel central redondeado (blanco)
        JPanel panelCentral = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setSize(377, 520); // Ajustamos el tamaño del panel central
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE); // Fondo blanco
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50); // Bordes redondeados
            }
        };
        panelCentral.setBounds(22, 50, 360, 580); // Ajustando la posición y tamaño para el panel central
        panelCentral.setLayout(null);
        panelCentral.setOpaque(false);

        // Título
        JLabel lblTitulo = new JLabel("   Login - Sistema de Monitoreo", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22)); // Ajustamos el tamaño de la fuente
        lblTitulo.setBounds(20, 30, 320, 30);
        panelCentral.add(lblTitulo);

        // Campo de correo
        JLabel lblCorreo = new JLabel("Email:");
        lblCorreo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCorreo.setBounds(40, 100, 220, 20);
        panelCentral.add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(40, 120, 280, 35); // Ajuste de tamaño para un campo más amplio
        txtCorreo.setFont(new Font("Arial", Font.PLAIN, 14));
        txtCorreo.setBorder(new RoundBorder(15)); // Bordes redondeados
        panelCentral.add(txtCorreo);

        // Campo de contraseña
        JLabel lblContraseña = new JLabel("Password:");
        lblContraseña.setFont(new Font("Arial", Font.PLAIN, 14));
        lblContraseña.setBounds(40, 170, 220, 20);
        panelCentral.add(lblContraseña);

        txtContraseña = new JPasswordField();
        txtContraseña.setBounds(40, 190, 280, 35); // Ajuste de tamaño para un campo más amplio
        txtContraseña.setFont(new Font("Arial", Font.PLAIN, 14));
        txtContraseña.setBorder(new RoundBorder(15)); // Bordes redondeados
        panelCentral.add(txtContraseña);

        // Botón de login
        btnLogin = new JButton("Login");
        btnLogin.setBounds(40, 250, 280, 40); // Ajuste de tamaño
        btnLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        btnLogin.setBackground(new Color(34, 139, 34)); // Verde oscuro
        btnLogin.setForeground(Color.WHITE); // Texto blanco
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        panelCentral.add(btnLogin);

        // Botón para registrar
        btnRegister = new JButton("Registro");
        btnRegister.setBounds(40, 310, 280, 40); // Ajuste de tamaño
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRegister.setForeground(new Color(0, 100, 0)); // Verde bosque
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        panelCentral.add(btnRegister);
        
        // Botón de cierre (X)
        JButton btnCerrar = new JButton("X");
        btnCerrar.setBounds(380, 10, 30, 30); // Ajuste la posición de la X
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(Color.RED);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder());
        btnCerrar.addActionListener(e -> System.exit(0)); // Cierra la aplicación
        panelFondo.add(btnCerrar);


        // Agregar panel central al panel de fondo
        panelFondo.add(panelCentral);

        // Establecer el contenido principal
        setContentPane(panelFondo);
    }

    // Clase para bordes redondeados
    static class RoundBorder extends AbstractBorder {
        private final int radius;

        public RoundBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY); // Color del borde
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 15, 5, 15); // Espaciado interno (top, left, bottom, right)
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = 5;
            insets.left = 15;
            insets.bottom = 5;
            insets.right = 15;
            return insets;
        }
    }
}
