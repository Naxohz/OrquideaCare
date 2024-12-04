package controller;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        vista.btnEliminarEvento.addActionListener(e -> eliminarEvento());
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

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            while (rs.next()) {
                Date fecha = rs.getDate("fecha_riego");
                String fechaFormateada = dateFormat.format(fecha);
                String hora = rs.getString("hora_riego");
                String descripcion = rs.getString("descripcion");
                modeloCalendario.addRow(new Object[]{fechaFormateada, hora, descripcion});
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

        int opcion = JOptionPane.showConfirmDialog(vista, campos, "Agregar Evento", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion == JOptionPane.OK_OPTION) {
            Date fecha = calendario.getDate();
            String hora = txtHora.getText();
            String descripcion = txtDescripcion.getText();

            if (fecha == null || hora.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!hora.matches("\\d{2}:\\d{2}")) {
                JOptionPane.showMessageDialog(vista, "El formato de la hora debe ser HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar que la hora no esté en el pasado
            Calendar fechaIngresada = Calendar.getInstance();
            fechaIngresada.setTime(fecha);

            String[] partesHora = hora.split(":");
            int horaIngresada = Integer.parseInt(partesHora[0]);
            int minutoIngresado = Integer.parseInt(partesHora[1]);

            fechaIngresada.set(Calendar.HOUR_OF_DAY, horaIngresada);
            fechaIngresada.set(Calendar.MINUTE, minutoIngresado);
            fechaIngresada.set(Calendar.SECOND, 0);

            Date ahora = new Date(); // Hora y fecha actuales
            if (fechaIngresada.getTime().before(ahora)) {
                JOptionPane.showMessageDialog(vista, "La fecha y hora no pueden ser en el pasado. Por favor, ingrese un horario válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Formatear la fecha para almacenar
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaStr = sdf.format(fecha);

            // Insertar en la base de datos
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


    private void eliminarEvento() {
        int filaSeleccionada = vista.tablaCalendario.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un evento para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de que desea eliminar este evento?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection conn = BaseDatos.conectar()) {
                String fecha = (String) modeloCalendario.getValueAt(filaSeleccionada, 0);
                String hora = (String) modeloCalendario.getValueAt(filaSeleccionada, 1);
                String sql = "DELETE FROM calendario_riego WHERE fecha_riego = ? AND hora_riego = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, fecha);
                stmt.setString(2, hora);
                stmt.executeUpdate();

                modeloCalendario.removeRow(filaSeleccionada);
                JOptionPane.showMessageDialog(vista, "Evento eliminado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(vista, "Error al eliminar el evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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

    // Variable para almacenar el intervalo actual
    private long intervaloActual = -1;

    private void aplicarIntervalo() {
        String seleccion = (String) vista.cmbIntervalo.getSelectedItem();
        long intervalo;

        // Determinar el valor del intervalo seleccionado
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

        // Verificar si el intervalo ya está aplicado
        if (intervalo == intervaloActual) {
            JOptionPane.showMessageDialog(vista, 
                "El intervalo seleccionado ya está en uso. Por favor, selecciona un intervalo diferente.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return; // Salir del método
        }

        // Actualizar el intervalo
        humedadController.iniciarRegistroAutomatico(vista, intervalo);
        intervaloActual = intervalo; // Guardar el nuevo intervalo actual
        JOptionPane.showMessageDialog(vista, "Intervalo actualizado a " + seleccion + ".");
    }
}