package view;

import javax.swing.*;
import java.awt.*;

public class PerfilView extends JPanel {
    public JLabel lblNombre, lblCorreo; // Etiquetas para mostrar los datos
    public JButton btnEditar, btnCerrarSesion; // Botones para editar y cerrar sesión

    public PerfilView(String nombre, String correo) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Título
        JLabel lblTitulo = new JLabel("Perfil del Agricultor");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);

        // Datos del perfil
        lblNombre = new JLabel("Nombre: " + nombre);
        lblCorreo = new JLabel("Correo: " + correo);

        lblNombre.setAlignmentX(CENTER_ALIGNMENT);
        lblCorreo.setAlignmentX(CENTER_ALIGNMENT);

        // Botón para editar
        btnEditar = new JButton("Editar Perfil");
        btnEditar.setAlignmentX(CENTER_ALIGNMENT);

        // Botón para cerrar sesión
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setAlignmentX(CENTER_ALIGNMENT);

        // Agregar componentes al panel
        add(lblTitulo);
        add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado
        add(lblNombre);
        add(lblCorreo);
        add(Box.createRigidArea(new Dimension(0, 20))); // Espaciado
        add(btnEditar);
        add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado
        add(btnCerrarSesion);
    }
}





