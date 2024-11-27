package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardView extends JFrame {
    public JLabel lblHumedadActual; // Etiqueta para mostrar la humedad actual
    public JLabel lblTemperaturaActual; // Etiqueta para mostrar la temperatura actual
    public JComboBox<String> cmbIntervalo; // ComboBox para seleccionar intervalo de actualización
    public JButton btnAplicarIntervalo; // Botón para aplicar el intervalo seleccionado
    public JButton btnAgregarEvento, btnVerHistorial; // Botones para otras acciones
    public JTable tablaCalendario, tablaHistorial; // Tablas para calendario e historial
    public JPanel panelPerfil; // Panel para mostrar el perfil del agricultor
    private DefaultTableModel modeloCalendario, modeloHistorial; // Modelos para las tablas

    public DashboardView() {
        setTitle("Dashboard - Sistema de Orquídeas");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel Superior: Mostrar Humedad y Temperatura
        JPanel panelSuperior = new JPanel(new GridLayout(3, 1));
        lblHumedadActual = new JLabel("Humedad Actual: Calculando...");
        lblHumedadActual.setFont(new Font("Arial", Font.BOLD, 16));
        lblHumedadActual.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblTemperaturaActual = new JLabel("Temperatura Actual: Calculando...");
        lblTemperaturaActual.setFont(new Font("Arial", Font.BOLD, 16));
        lblTemperaturaActual.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Selección de intervalo
        JPanel panelIntervalo = new JPanel(new FlowLayout());
        JLabel lblIntervalo = new JLabel("Actualizar cada:");
        cmbIntervalo = new JComboBox<>(new String[]{"1 minuto", "3 minutos", "5 minutos", "10 minutos", "30 minutos", "1 hora"});
        btnAplicarIntervalo = new JButton("Aplicar");

        panelIntervalo.add(lblIntervalo);
        panelIntervalo.add(cmbIntervalo);
        panelIntervalo.add(btnAplicarIntervalo);

        panelSuperior.add(lblHumedadActual);
        panelSuperior.add(lblTemperaturaActual);
        panelSuperior.add(panelIntervalo);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel Central: Dividido en dos partes (Calendario de Riego y Historial de Humedad)
        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 10, 10));

        // Modelo y Tabla para Calendario de Riego
        String[] columnasCalendario = {"Fecha", "Hora", "Descripción"};
        modeloCalendario = new DefaultTableModel(columnasCalendario, 0);
        tablaCalendario = new JTable(modeloCalendario);
        JScrollPane scrollCalendario = new JScrollPane(tablaCalendario);
        scrollCalendario.setBorder(BorderFactory.createTitledBorder("Calendario de Riego"));

        btnAgregarEvento = new JButton("Agregar Evento");
        JPanel panelCalendario = new JPanel(new BorderLayout());
        panelCalendario.add(scrollCalendario, BorderLayout.CENTER);
        panelCalendario.add(btnAgregarEvento, BorderLayout.SOUTH);

        panelCentral.add(panelCalendario);

        // Modelo y Tabla para Historial de Humedad
        String[] columnasHistorial = {"Fecha", "Hora", "Humedad", "Temperatura"};
        modeloHistorial = new DefaultTableModel(columnasHistorial, 0);
        tablaHistorial = new JTable(modeloHistorial);
        JScrollPane scrollHistorial = new JScrollPane(tablaHistorial);
        scrollHistorial.setBorder(BorderFactory.createTitledBorder("Historial de Humedad y Temperatura"));

        btnVerHistorial = new JButton("Ver Historial");
        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.add(scrollHistorial, BorderLayout.CENTER);
        panelHistorial.add(btnVerHistorial, BorderLayout.SOUTH);

        panelCentral.add(panelHistorial);

        add(panelCentral, BorderLayout.CENTER);

        // Panel Izquierdo: Mostrar Perfil del Agricultor
        panelPerfil = new JPanel();
        panelPerfil.setLayout(new BorderLayout());
        panelPerfil.setBorder(BorderFactory.createTitledBorder("Perfil del Agricultor"));
        panelPerfil.setPreferredSize(new Dimension(300, 0));
        add(panelPerfil, BorderLayout.WEST);
    }

    public DefaultTableModel getModeloCalendario() {
        return modeloCalendario;
    }

    public DefaultTableModel getModeloHistorial() {
        return modeloHistorial;
    }
}













