package controller;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.BaseDatos;
import view.DashboardView;

public class CalendarioController {
    private DashboardView vista;
    private DefaultTableModel modeloCalendario;

    public CalendarioController(DashboardView vista) {
        this.vista = vista;

        modeloCalendario = (DefaultTableModel) vista.tablaCalendario.getModel();
        vista.btnAgregarEvento.addActionListener(e -> agregarEvento());
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

            int opcion = JOptionPane.showConfirmDialog(null, campos, "Agregar Evento", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                Date fecha = calendario.getDate();
                String hora = txtHora.getText();
                String descripcion = txtDescripcion.getText();

                if (fecha == null || hora.isEmpty() || descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                    return;
                }

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

                // Agregar al modelo de la tabla
                modeloCalendario.addRow(new Object[]{fechaStr, hora, descripcion});
                JOptionPane.showMessageDialog(null, "Evento agregado correctamente.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al agregar el evento: " + ex.getMessage());
        }
    }
}






