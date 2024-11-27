package main;

import view.LoginView;
import controller.LoginController;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Configuración de apariencia (opcional)
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf"); // FlatLaf para apariencia moderna
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Iniciar Login
        LoginView loginView = new LoginView();
        new LoginController(loginView);
        loginView.setVisible(true);
    }
}



