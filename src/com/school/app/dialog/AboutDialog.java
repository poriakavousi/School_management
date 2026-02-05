package com.school.app.dialog;

import javax.swing.*;
import java.awt.*;

public class AboutDialog {

    public static void show(Component parent) {
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(parent),
                "درباره برنامه",
                true
        );

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // هدر
        JLabel titleLabel = new JLabel(
                "<html><h1>برنامه مدیریت مدرسه</h1></html>",
                JLabel.CENTER
        );
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));

        // اطلاعات
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setBackground(panel.getBackground());
        infoArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
        infoArea.setText(
                "نسخه: 1.0.0\n" +
                        "توسعه‌دهنده: تیم نرم‌افزار مدرسه\n" +
                        "تاریخ انتشار: ۱۴۰۳\n" +
                        "\n" +
                        "امکانات:\n" +
                        "• مدیریت دانش‌آموزان\n" +
                        "• احراز هویت کاربران\n" +
                        "• رابط کاربری گرافیکی\n" +
                        "• ذخیره‌سازی در پایگاه داده MySQL\n" +
                        "\n" +
                        "تماس: info@schoolapp.ir"
        );

        JButton closeButton = new JButton("بستن");

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setContentPane(panel);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}