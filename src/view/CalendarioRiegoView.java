package view;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;

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
    }
}



