package com.school.app;

import com.school.app.repository.StudentRepositoryMySql;
import com.school.app.repository.UserRepositoryMySql;
import com.school.app.service.AuthService;
import com.school.app.service.StudentService;
import com.school.app.service.StudentServiceImpl;
import com.school.app.ui.LoginFrame;
import com.school.app.ui.SchoolAppFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                setupUITheme();
            } catch (Exception e) {
                System.err.println("خطا در تنظیم ظاهر سیستم: " + e.getMessage());
            }

            AuthService authService = new AuthService(new UserRepositoryMySql());
            StudentService studentService = new StudentServiceImpl(new StudentRepositoryMySql());

            LoginFrame loginFrame = new LoginFrame(authService, () -> {
                SchoolAppFrame mainApp = new SchoolAppFrame(studentService);
                mainApp.setVisible(true);
            });
            loginFrame.setVisible(true);
        });
    }

    private static void setupUITheme() {
        try {
            UIManager.put("Button.font", new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
            UIManager.put("Label.font", new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
            UIManager.put("TextField.font", new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
            UIManager.put("TextArea.font", new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
            UIManager.put("Table.font", new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
            UIManager.put("TableHeader.font", new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12));
        } catch (Exception e) {
        }
    }
}