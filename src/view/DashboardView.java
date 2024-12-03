package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.sql.*;
import model.BaseDatos;

public class DashboardView extends JFrame {
    public JLabel lblHumedadActual, lblTemperaturaActual;
    public JComboBox<String> cmbIntervalo;
    public JButton btnAplicarIntervalo, btnAgregarEvento, btnEliminarEvento, btnVerHistorial;
    public JTable tablaCalendario, tablaHistorial;
    public JPanel panelPerfil;
    private DefaultTableModel modeloCalendario, modeloHistorial;
    public Timer timer; // Timer para verificar la hora de riego

    public DashboardView() {
        setTitle("Dashboard - Sistema de Orquídeas");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel Principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(new Color(153, 186, 146));
        add(panelPrincipal, BorderLayout.CENTER);

        // Panel Izquierdo: Perfil del Agricultor
        panelPerfil = new JPanel();
        panelPerfil.setLayout(new BoxLayout(panelPerfil, BoxLayout.Y_AXIS));
        panelPerfil.setBorder(BorderFactory.createTitledBorder("Perfil del Agricultor"));
        panelPerfil.setPreferredSize(new Dimension(300, 0));
        panelPerfil.setBackground(new Color(153, 186, 146));
        panelPrincipal.add(panelPerfil, BorderLayout.WEST);

        // Panel Central: Mostrar Humedad, Temperatura y Intervalo
        JPanel panelInfoOrquideas = new JPanel();
        panelInfoOrquideas.setLayout(new GridLayout(3, 1, 5, 5));
        panelInfoOrquideas.setBorder(BorderFactory.createTitledBorder("Información de las Orquídeas"));
        panelInfoOrquideas.setBackground(new Color(153, 186, 146));
        panelPrincipal.add(panelInfoOrquideas, BorderLayout.CENTER);

        lblHumedadActual = new JLabel("Humedad Actual: Calculando...", JLabel.CENTER);
        lblHumedadActual.setFont(new Font("Arial", Font.BOLD, 16));
        panelInfoOrquideas.add(lblHumedadActual);

        lblTemperaturaActual = new JLabel("Temperatura Actual: Calculando...", JLabel.CENTER);
        lblTemperaturaActual.setFont(new Font("Arial", Font.BOLD, 16));
        panelInfoOrquideas.add(lblTemperaturaActual);

        // Panel para seleccionar intervalo
        JPanel panelIntervalo = new JPanel(new FlowLayout());
        panelIntervalo.setBackground(new Color(153, 186, 146));
        JLabel lblIntervalo = new JLabel("Actualizar cada:");
        cmbIntervalo = new JComboBox<>(new String[]{"1 minuto", "3 minutos", "5 minutos", "10 minutos", "30 minutos", "1 hora"});
        btnAplicarIntervalo = new JButton("Aplicar");

        panelIntervalo.add(lblIntervalo);
        panelIntervalo.add(cmbIntervalo);
        panelIntervalo.add(btnAplicarIntervalo);

        panelInfoOrquideas.add(panelIntervalo);

        // Panel Inferior: Calendario de Riego y Historial
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new GridLayout(1, 2, 10, 10));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // Calendario de Riego
        JPanel panelCalendario = new JPanel(new BorderLayout());
        panelCalendario.setBackground(new Color(153, 186, 146)); // Fondo verde claro
        panelCalendario.setBorder(BorderFactory.createTitledBorder("Calendario de Riego"));
        modeloCalendario = new DefaultTableModel(new String[]{"Fecha", "Hora", "Descripción"}, 0);
        tablaCalendario = new JTable(modeloCalendario);
        JScrollPane scrollCalendario = new JScrollPane(tablaCalendario);

        // Botones para el calendario
        JPanel panelBotonesCalendario = new JPanel(new FlowLayout());
        btnAgregarEvento = new JButton("Agregar Evento");
        btnEliminarEvento = new JButton("Eliminar Evento");
        panelBotonesCalendario.add(btnAgregarEvento);
        panelBotonesCalendario.add(btnEliminarEvento);

        panelCalendario.add(scrollCalendario, BorderLayout.CENTER);
        panelCalendario.add(panelBotonesCalendario, BorderLayout.SOUTH);

        panelInferior.add(panelCalendario);

        // Historial de Humedad
        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.setBackground(new Color(153, 186, 146)); // Fondo verde claro
        panelHistorial.setBorder(BorderFactory.createTitledBorder("Historial de Humedad y Temperatura"));
        modeloHistorial = new DefaultTableModel(new String[]{"Fecha", "Hora", "Humedad", "Temperatura"}, 0);
        tablaHistorial = new JTable(modeloHistorial);
        JScrollPane scrollHistorial = new JScrollPane(tablaHistorial);
        panelHistorial.add(scrollHistorial, BorderLayout.CENTER);
        btnVerHistorial = new JButton("Ver Historial");
        panelHistorial.add(btnVerHistorial, BorderLayout.SOUTH);
        panelInferior.add(panelHistorial);

        // Estilo para el Panel Principal
        panelInferior.setBackground(new Color(153, 186, 146));

        // Iniciar el temporizador para la actualización en tiempo real
        iniciarTemporizador();
    }

    // Método para iniciar el temporizador que verifica la hora de riego cada minuto
    private void iniciarTemporizador() {
        // Crea un hilo para introducir el retraso de 1 segundo al iniciar sesión
        new Thread(() -> {
            try {
                // Retrasa la ejecución durante 1 segundo (1000 ms)
                Thread.sleep(1000); 

                // Después del retraso, verifica la hora de riego
                verificarHoraDeRiego();  // Verifica al instante si es hora de riego

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // Luego, inicia el temporizador que sigue verificando la hora de riego cada 10 segundos
        timer = new Timer(10000, e -> verificarHoraDeRiego()); // Cada 10,000 ms (10 segundos)
        timer.start();
    }

    // Método para verificar la hora de riego en la base de datos
    private void verificarHoraDeRiego() {
        try {
            String sql = "SELECT id, descripcion, fecha_riego, hora_riego FROM calendario_riego WHERE fecha_riego = ? AND hora_riego = ? AND notificado = FALSE";
            Connection conn = BaseDatos.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Obtener la hora actual y formatearla
            SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
            String fechaActual = sdfFecha.format(new java.util.Date());
            String horaActual = sdfHora.format(new java.util.Date());

            stmt.setString(1, fechaActual); // Fecha actual
            stmt.setString(2, horaActual);  // Hora actual

            ResultSet rs = stmt.executeQuery();

            // Si hay un evento de riego que coincide con la fecha y hora actuales
            if (rs.next()) {
                int idEvento = rs.getInt("id");
                String descripcion = rs.getString("descripcion");

                mostrarNotificacionRiego(descripcion); // Mostrar la notificación al instante

                // Después de mostrar la notificación, marcar el evento como notificado
                actualizarNotificado(idEvento);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    // Método para actualizar la base de datos y marcar como notificado
    private void actualizarNotificado(int idEvento) {
        try {
            String sql = "UPDATE calendario_riego SET notificado = TRUE WHERE id = ?";
            Connection conn = BaseDatos.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEvento); // ID del evento que ya ha sido notificado
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Método para mostrar la notificación de riego
    public void mostrarNotificacionRiego(String descripcion) {
        // Mostrar solo el mensaje y la descripción del evento
        JOptionPane.showMessageDialog(this, 
            "¡Es hora de regar las orquídeas!\n\n" + 
            "Descripción: " + descripcion, 
            "Notificación de Riego", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    public DefaultTableModel getModeloCalendario() {
        return modeloCalendario;
    }

    public DefaultTableModel getModeloHistorial() {
        return modeloHistorial;
    }

    // Detener el temporizador al cerrar sesión
    @Override
    public void dispose() {
        if (timer != null) {
            timer.stop(); 
        }
        super.dispose();
    }
}
