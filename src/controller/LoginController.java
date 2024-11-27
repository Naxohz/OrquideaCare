package controller;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import model.BaseDatos;
import view.DashboardView;
import view.LoginView;

public class LoginController {
    private LoginView vista;

    public LoginController(LoginView vista) {
        this.vista = vista;

        // Configurar acciones para los botones
        this.vista.btnLogin.addActionListener(e -> validarLogin());
        this.vista.btnRegister.addActionListener(e -> registrarUsuario());
    }

    private void validarLogin() {
        try (Connection conn = BaseDatos.conectar()) {
            String correo = vista.txtCorreo.getText();
            String contraseña = new String(vista.txtContraseña.getPassword());

            // Validar que los campos no estén vacíos
            if (correo.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor, ingrese su correo y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Consulta para validar credenciales
            String sql = "SELECT * FROM agricultores WHERE correo = ? AND contraseña = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            stmt.setString(2, contraseña);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                // Credenciales correctas: Abrir el Dashboard
                JOptionPane.showMessageDialog(vista, "Inicio de sesión exitoso. Bienvenido, " + rs.getString("nombre") + "!");
                abrirDashboard(correo); // Pasar el correo al DashboardController
                vista.dispose(); // Cerrar la ventana de login
            } else {
                // Credenciales incorrectas
                JOptionPane.showMessageDialog(vista, "Correo o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al intentar iniciar sesión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarUsuario() {
        try (Connection conn = BaseDatos.conectar()) {
            // Crear un panel personalizado para el cuadro de diálogo
            JTextField txtNombre = new JTextField();
            JTextField txtCorreo = new JTextField();
            JPasswordField txtContraseña = new JPasswordField();

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel("Nombre:"));
            panel.add(txtNombre);
            panel.add(Box.createVerticalStrut(10)); // Espaciado
            panel.add(new JLabel("Correo:"));
            panel.add(txtCorreo);
            panel.add(Box.createVerticalStrut(10)); // Espaciado
            panel.add(new JLabel("Contraseña:"));
            panel.add(txtContraseña);

            int opcion = JOptionPane.showConfirmDialog(vista, panel, "Registrar Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (opcion == JOptionPane.OK_OPTION) {
                String nombre = txtNombre.getText();
                String correo = txtCorreo.getText();
                String contraseña = new String(txtContraseña.getPassword());

                // Validar que los campos no estén vacíos
                if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insertar el nuevo usuario en la base de datos
                String sql = "INSERT INTO agricultores (nombre, correo, contraseña) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setString(2, correo);
                stmt.setString(3, contraseña);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(vista, "Usuario registrado exitosamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al registrar el usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDashboard(String correo) {
        // Crear el Dashboard y pasarlo al controlador
        DashboardView dashboardView = new DashboardView();
        new DashboardController(dashboardView, correo);
        dashboardView.setVisible(true);
    }
}