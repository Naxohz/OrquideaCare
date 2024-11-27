package controller;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.BaseDatos;
import view.DashboardView;
import view.LoginView;
import view.PerfilView;

public class DashboardController {
    private DashboardView vista;
    private DefaultTableModel modeloCalendario, modeloHistorial;
    private HumedadController humedadController;

    public DashboardController(DashboardView vista, String correo) {
        this.vista = vista;
        this.humedadController = new HumedadController();

        // Inicializar modelos de tablas
        this.modeloCalendario = vista.getModeloCalendario();
        this.modeloHistorial = vista.getModeloHistorial();

        // Configurar acciones de los botones
        vista.btnAplicarIntervalo.addActionListener(e -> aplicarIntervalo());
        vista.btnAgregarEvento.addActionListener(e -> agregarEvento());
        vista.btnVerHistorial.addActionListener(e -> cargarHistorialHumedad());

        // Cargar datos iniciales
        cargarPerfil(correo);
        cargarEventosCalendario();

        // Iniciar con un intervalo predeterminado de 5 minutos
        humedadController.iniciarRegistroAutomatico(vista, 5 * 60 * 1000);
    }

    private void cargarPerfil(String correo) {
        try (Connection conn = BaseDatos.conectar()) {
            String sql = "SELECT nombre, correo FROM agricultores WHERE correo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String correoAgricultor = rs.getString("correo");

                // Crear la vista del perfil
                PerfilView perfilView = new PerfilView(nombre, correoAgricultor);

                // Acción para el botón Editar
                perfilView.btnEditar.addActionListener(e -> editarPerfil(correo));

                // Acción para el botón Cerrar Sesión
                perfilView.btnCerrarSesion.addActionListener(e -> cerrarSesion());

                // Agregar perfil al panel del dashboard
                vista.panelPerfil.removeAll();
                vista.panelPerfil.add(perfilView);
                vista.panelPerfil.revalidate();
                vista.panelPerfil.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al cargar el perfil: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarPerfil(String correo) {
        try (Connection conn = BaseDatos.conectar()) {
            String nuevoNombre = JOptionPane.showInputDialog(vista, "Ingrese el nuevo nombre:");
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El nombre no puede estar vacío.");
                return;
            }

            String sql = "UPDATE agricultores SET nombre = ? WHERE correo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevoNombre);
            stmt.setString(2, correo);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(vista, "Perfil actualizado correctamente.");
            cargarPerfil(correo);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al actualizar el perfil: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(vista, "¿Está seguro de que desea cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            vista.dispose();
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        }
    }

    private void cargarEventosCalendario() {
        try (Connection conn = BaseDatos.conectar()) {
            String sql = "SELECT fecha_riego, hora_riego, descripcion FROM calendario_riego";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Limpiar la tabla antes de cargar nuevos datos
            modeloCalendario.setRowCount(0);

            while (rs.next()) {
                String fecha = rs.getString("fecha_riego");
                String hora = rs.getString("hora_riego");
                String descripcion = rs.getString("descripcion");
                modeloCalendario.addRow(new Object[]{fecha, hora, descripcion});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al cargar los eventos del calendario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarEvento() {
        try {
            JDateChooser calendario = new JDateChooser();
            JTextField txtHora = new JTextField();
            JTextArea txtDescripcion = new JTextArea();

            Object[] campos = {
                "Fecha:", calendario,
                "Hora (HH:mm):", txtHora,
                "Descripción:", txtDescripcion
            };

            int opcion = JOptionPane.showConfirmDialog(vista, campos, "Agregar Evento", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                Date fecha = calendario.getDate();
                String hora = txtHora.getText();
                String descripcion = txtDescripcion.getText();

                if (fecha == null || hora.isEmpty() || descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                    return;
                }

                if (!hora.matches("\\d{2}:\\d{2}")) {
                    JOptionPane.showMessageDialog(vista, "El formato de la hora debe ser HH:mm.");
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaStr = sdf.format(fecha);

                Connection conn = BaseDatos.conectar();
                String sql = "INSERT INTO calendario_riego (fecha_riego, hora_riego, descripcion) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, fechaStr);
                stmt.setString(2, hora);
                stmt.setString(3, descripcion);
                stmt.executeUpdate();

                modeloCalendario.addRow(new Object[]{fechaStr, hora, descripcion});
                JOptionPane.showMessageDialog(vista, "Evento agregado correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al agregar el evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorialHumedad() {
        try (Connection conn = BaseDatos.conectar()) {
            String sql = "SELECT fecha_registro, humedad, temperatura FROM humedad_historica ORDER BY fecha_registro DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Limpiar la tabla antes de cargar nuevos datos
            modeloHistorial.setRowCount(0);

            while (rs.next()) {
                String fechaHora = rs.getString("fecha_registro");
                String[] partesFechaHora = fechaHora.split(" ");
                String fecha = partesFechaHora[0];
                String hora = partesFechaHora[1];
                double humedad = rs.getDouble("humedad");
                double temperatura = rs.getDouble("temperatura");

                modeloHistorial.addRow(new Object[]{fecha, hora, String.format("%.2f%%", humedad), String.format("%.2f°C", temperatura)});
            }

            JOptionPane.showMessageDialog(vista, "Historial de humedad cargado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al cargar el historial de humedad: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarIntervalo() {
        String seleccion = (String) vista.cmbIntervalo.getSelectedItem();
        long intervalo;

        switch (seleccion) {
            case "1 minuto":
                intervalo = 1 * 60 * 1000;
                break;
            case "3 minutos":
                intervalo = 3 * 60 * 1000;
                break;
            case "5 minutos":
                intervalo = 5 * 60 * 1000;
                break;
            case "10 minutos":
                intervalo = 10 * 60 * 1000;
                break;
            case "30 minutos":
                intervalo = 30 * 60 * 1000;
                break;
            case "1 hora":
                intervalo = 60 * 60 * 1000;
                break;
            default:
                intervalo = 5 * 60 * 1000; // Valor por defecto
        }

        humedadController.iniciarRegistroAutomatico(vista, intervalo);
        JOptionPane.showMessageDialog(vista, "Intervalo actualizado a " + seleccion + ".");
    }
}












