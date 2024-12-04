package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class PerfilView extends JPanel {
    public JLabel lblNombre, lblCorreo; // Etiquetas para mostrar los datos
    public JButton btnEditar, btnCerrarSesion; // Botones para editar y cerrar sesión

    public PerfilView(String nombre, String correo) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(111, 155, 122)); // Fondo suave
        // Estilo para el Título
        JLabel lblTitulo = new JLabel("Información del Perfil", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);

        // Panel para mostrar los datos
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new GridLayout(2, 1, 5, 5)); // Para nombre y correo
        panelDatos.setBackground(new Color(111, 155, 122));

        lblNombre = new JLabel("Nombre: " + nombre);
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCorreo = new JLabel("Correo: " + correo);
        lblCorreo.setFont(new Font("Arial", Font.PLAIN, 14));

        panelDatos.add(lblNombre);
        panelDatos.add(lblCorreo);

        // Panel para botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Espaciado entre botones
        panelBotones.setBackground(new Color(111, 155, 122));;
        
        // Botón Editar Perfil
        btnEditar = new JButton("Editar Perfil");
        btnEditar.setPreferredSize(new Dimension(100, 30)); // Tamaño ajustado
        btnEditar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEditar.setBackground(new Color(34, 70, 27)); // Verde suave
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Botón Cerrar Sesión
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setPreferredSize(new Dimension(120, 30)); // Tamaño ajustado
        btnCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCerrarSesion.setBackground(new Color(68, 18, 18)); // Rojo suave
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Añadir los botones al panel
        panelBotones.add(btnEditar);
        panelBotones.add(btnCerrarSesion);

        // Añadir componentes al panel principal
        add(lblTitulo);
        add(Box.createRigidArea(new Dimension(0, 1))); // Espaciado
        add(panelDatos);
        add(Box.createRigidArea(new Dimension(0, 1))); // Espaciado
        add(panelBotones);
    }
}