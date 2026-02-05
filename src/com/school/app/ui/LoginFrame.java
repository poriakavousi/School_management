package com.school.app.ui;

import com.school.app.dialog.MessageDialog;
import com.school.app.model.Response;
import com.school.app.model.User;
import com.school.app.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final AuthService authService;
    private final Runnable onLoginSuccess;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JButton loginBtn = new JButton("ورود");
    private final JButton registerBtn = new JButton("ثبت‌نام");
    private final JButton exitBtn = new JButton("خروج");

    public LoginFrame(AuthService authService, Runnable onLoginSuccess) {
        this.authService = authService;
        this.onLoginSuccess = onLoginSuccess;

        authService.setParentComponent(this);

        initializeUI();
        setupEventHandlers();
    }

    private void initializeUI() {
        setTitle("ورود به سیستم");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("   پوریا کاوسی ، سرور آنبسته زادگان", JLabel.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("نام کاربری:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("رمز عبور:"));
        formPanel.add(passwordField);

        formPanel.add(loginBtn);
        formPanel.add(registerBtn);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitPanel.add(exitBtn);
        mainPanel.add(exitPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        loginBtn.addActionListener(e -> performLogin());

        registerBtn.addActionListener(e -> openRegisterFrame());

        exitBtn.addActionListener(e -> {
            if (MessageDialog.showConfirm(this, "خروج", "آیا از خروج از برنامه اطمینان دارید؟")) {
                System.exit(0);
            }
        });

        usernameField.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            MessageDialog.showError(this, "لطفاً نام کاربری و رمز عبور را وارد کنید");
            return;
        }

        try {
            Response<User> result = authService.login(username, password);

            if (result.getStatus() < 300) {
                User user = result.getData();
                MessageDialog.showSuccess(this,
                        "خوش آمدید " + user.getFullName() + "!\n" +
                                "ورود شما با موفقیت انجام شد.");

                dispose();
                onLoginSuccess.run();
            }
        } catch (Exception e) {
            MessageDialog.showError(this, "خطای غیرمنتظره: " + e.getMessage());
        }
    }

    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame(authService, () -> {
            this.setVisible(true);
            clearForm();
        });
        registerFrame.setVisible(true);
        this.setVisible(false);
    }

    private void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
    }
}