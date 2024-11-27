package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import model.BaseDatos;
import view.DashboardView;

public class HumedadController {
    private Timer timer;
    private double humedadActual;
    private double temperaturaActual;

    public void iniciarRegistroAutomatico(DashboardView vista, long intervalo) {
        detenerRegistro(); // Detener cualquier temporizador previo
        timer = new Timer(true); // "true" para que sea un hilo de demonio
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                generarDatos();
                actualizarVista(vista);
            }
        }, 0, intervalo);
    }

    private void generarDatos() {
        try {
            Random random = new Random();

            // Generar humedad entre 70% y 90%
            humedadActual = 70 + (random.nextDouble() * 20);
            humedadActual = Math.round(humedadActual * 100.0) / 100.0; // Redondear a 2 decimales

            // Generar temperatura entre 18°C y 25°C
            temperaturaActual = 18 + (random.nextDouble() * 7);
            temperaturaActual = Math.round(temperaturaActual * 100.0) / 100.0; // Redondear a 2 decimales

            // Insertar en la base de datos
            Connection conn = BaseDatos.conectar();
            String sql = "INSERT INTO humedad_historica (humedad, temperatura, fecha_registro) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, humedadActual);
            stmt.setDouble(2, temperaturaActual);
            stmt.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarVista(DashboardView vista) {
        vista.lblHumedadActual.setText("Humedad Actual: " + humedadActual + "%");
        vista.lblTemperaturaActual.setText("Temperatura Actual: " + temperaturaActual + "°C");
    }

    public void detenerRegistro() {
        if (timer != null) {
            timer.cancel();
        }
    }
}







