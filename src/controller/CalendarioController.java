package controller;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import model.BaseDatos;
import view.DashboardView;

public class CalendarioController {
    private DashboardView vista;
    private Timer timer;

    public CalendarioController(DashboardView vista) {
        this.vista = vista;
        timer = new Timer();
        // Iniciar la verificación de eventos de riego cada minuto
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                verificarEventosDeRiego();
            }
        }, 0, 60 * 1000); // Verificar cada 60 segundos
    }

    private void verificarEventosDeRiego() {
        try {
            // Obtener la fecha y hora actuales
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String fechaHoraActual = sdf.format(new Date());  // Formato: 2024-12-01 14:30

            // Consulta para obtener los eventos de riego
            String sql = "SELECT fecha_riego, hora_riego, descripcion FROM calendario_riego";
            Connection conn = BaseDatos.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                String fechaRiego = rs.getString("fecha_riego");
                String horaRiego = rs.getString("hora_riego");
                String descripcion = rs.getString("descripcion");

                // Concatenar la fecha y la hora para compararla con la fecha/hora actual
                String eventoRiego = fechaRiego + " " + horaRiego;

                // Si la fecha y hora del evento coinciden con la fecha y hora actuales
                if (eventoRiego.equals(fechaHoraActual)) {
                    mostrarNotificacionRiego(descripcion); // Mostrar notificación con descripción
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarNotificacionRiego(String descripcion) {
        // Mostrar un cuadro de diálogo con la notificación
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(vista, 
                "¡Es hora de regar las orquídeas!\nDescripción: " + descripcion, 
                "Notificación de Riego", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // Detener la verificación de eventos cuando ya no sea necesario
    public void detenerVerificacion() {
        if (timer != null) {
            timer.cancel();
        }
    }
}