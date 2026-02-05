package com.school.app.dialog;

import javax.swing.*;
import java.awt.*;

public class MessageDialog {

    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "موفقیت",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "خطا",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "هشدار",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "اطلاعات",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(
                parent,
                message,
                "تایید",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    public static boolean showConfirm(Component parent, String title, String message) {
        int result = JOptionPane.showConfirmDialog(
                parent,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    public static int showCustomConfirm(Component parent, String title, String message,
                                        String[] options, String defaultOption) {
        return JOptionPane.showOptionDialog(
                parent,
                message,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                defaultOption
        );
    }

    public static String showInputDialog(Component parent, String message) {
        return JOptionPane.showInputDialog(parent, message);
    }

    public static String showInputDialog(Component parent, String message, String defaultValue) {
        return (String) JOptionPane.showInputDialog(
                parent,
                message,
                "ورودی",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                defaultValue
        );
    }
}