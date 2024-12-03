package view;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import model.BaseDatos;

public class CalendarioRiegoView extends JFrame {
    public JDateChooser calendario; // Componente JCalendar
    public JTextField txtHora;
    public JTextArea txtDescripcion;
    public JButton btnGuardar;

    public CalendarioRiegoView() {
        setTitle("Calendario de Riego");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Calendario de Riego", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Panel Central
        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        calendario = new JDateChooser();
        calendario.setDateFormatString("yyyy-MM-dd");

        txtHora = new JTextField();
        txtHora.setToolTipText("Formato: HH:mm (Ej: 14:30)");

        txtDescripcion = new JTextArea(5, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        panelCentral.add(new JLabel("Fecha de Riego:"));
        panelCentral.add(calendario);
        panelCentral.add(new JLabel("Hora de Riego:"));
        panelCentral.add(txtHora);
        panelCentral.add(new JLabel("Descripción:"));
        panelCentral.add(new JScrollPane(txtDescripcion));

        // Botón Guardar
        btnGuardar = new JButton("Guardar Evento");
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnGuardar);

        add(lblTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        // Lógica del botón guardar
        btnGuardar.addActionListener(e -> guardarEventoRiego());
    }

    private void guardarEventoRiego() {
        // Obtener los datos del formulario
        String fecha = ((JTextField) calendario.getDateEditor().getUiComponent()).getText();
        String hora = txtHora.getText();
        String descripcion = txtDescripcion.getText();

        if (fecha.isEmpty() || hora.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        // Validar formato de la hora (HH:mm)
        if (!hora.matches("\\d{2}:\\d{2}")) {
            JOptionPane.showMessageDialog(this, "El formato de la hora debe ser HH:mm.");
            return;
        }

        // Eliminar los segundos en la hora
        if (hora.length() == 5) {
            // Si la hora está en formato HH:mm, no hacemos nada.
        } else {
            // Si el formato tiene segundos, eliminamos los segundos
            hora = hora.substring(0, 5);
        }

        // Formatear la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaFormateada = sdf.format(calendario.getDate());

        // Insertar en la base de datos
        try {
            String sql = "INSERT INTO calendario_riego (fecha_riego, hora_riego, descripcion) VALUES (?, ?, ?)";
            Connection conn = BaseDatos.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fechaFormateada); // Fecha de riego
            stmt.setString(2, hora); // Hora de riego (sin segundos)
            stmt.setString(3, descripcion); // Descripción del evento
            stmt.executeUpdate();

            // Mensaje de éxito
            JOptionPane.showMessageDialog(this, "Evento de riego agregado correctamente.");
            this.dispose(); // Cierra la ventana después de guardar el evento
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al agregar el evento: " + ex.getMessage());
        }
    }
}