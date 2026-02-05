package com.school.app.ui;

import com.school.app.dialog.MessageDialog;
import com.school.app.model.Response;
import com.school.app.model.User;
import com.school.app.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final AuthService authService;
    private final Runnable backToLogin;

    private final JTextField fullNameField = new JTextField(15);
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField confirmPasswordField = new JPasswordField(15);
    private final JButton registerBtn = new JButton("ثبت‌نام");
    private final JButton backBtn = new JButton("بازگشت");

    public RegisterFrame(AuthService authService, Runnable backToLogin) {
        this.authService = authService;
        this.backToLogin = backToLogin;

        authService.setParentComponent(this);

        initializeUI();
        setupEventHandlers();
    }

    private void initializeUI() {
        setTitle("ثبت‌نام کاربر جدید");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // پنل اصلی
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // عنوان
        JLabel titleLabel = new JLabel("ایجاد حساب کاربری", JLabel.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // پنل فرم
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("نام کامل:"));
        formPanel.add(fullNameField);

        formPanel.add(new JLabel("نام کاربری:"));
        formPanel.add(usernameField);

        formPanel.add(new JLabel("رمز عبور:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("تأیید رمز عبور:"));
        formPanel.add(confirmPasswordField);

        formPanel.add(registerBtn);
        formPanel.add(backBtn);

        mainPanel.add(formPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        registerBtn.addActionListener(e -> performRegister());

        backBtn.addActionListener(e -> {
            dispose();
            backToLogin.run();
        });

        fullNameField.addActionListener(e -> performRegister());
        usernameField.addActionListener(e -> performRegister());
        passwordField.addActionListener(e -> performRegister());
        confirmPasswordField.addActionListener(e -> performRegister());
    }

    private void performRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            MessageDialog.showError(this, "لطفاً تمام فیلدها را تکمیل کنید");
            return;
        }

        if (!password.equals(confirmPassword)) {
            MessageDialog.showError(this, "رمز عبور و تأیید رمز عبور مطابقت ندارند");
            passwordField.setText("");
            confirmPasswordField.setText("");
            passwordField.requestFocus();
            return;
        }

        if (password.length() < 4) {
            MessageDialog.showError(this, "رمز عبور باید حداقل ۴ کاراکتر باشد");
            passwordField.requestFocus();
            return;
        }

        try {
            Response<User> result = authService.register(username, password, fullName);

            if (result.getStatus() < 300) {
                MessageDialog.showSuccess(this,
                        "ثبت‌نام با موفقیت انجام شد!\n\n" +
                                "اکنون می‌توانید با نام کاربری خود وارد شوید.");

                dispose();
                backToLogin.run();
            }
        } catch (Exception e) {
            MessageDialog.showError(this, "خطای غیرمنتظره: " + e.getMessage());
        }
    }
}