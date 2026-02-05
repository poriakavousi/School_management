package com.school.app.dialog;

import com.school.app.dto.StudentRequest;
import com.school.app.dto.StudentResponse;
import com.school.app.dto.mapper.StudentMapper;
import com.school.app.model.Student;

import javax.swing.*;
import java.awt.*;

public class StudentDialog {

    public static StudentRequest showCreateDialog(Component parent) {
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(parent),
                "ایجاد دانش‌آموز جدید",
                true
        );

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nationalCodeField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField classNameField = new JTextField();

        panel.add(new JLabel("کد ملی (۱۰ رقم):"));
        panel.add(nationalCodeField);
        panel.add(new JLabel("نام:"));
        panel.add(firstNameField);
        panel.add(new JLabel("نام خانوادگی:"));
        panel.add(lastNameField);
        panel.add(new JLabel("کلاس:"));
        panel.add(classNameField);

        JButton saveButton = new JButton("ذخیره");
        JButton cancelButton = new JButton("لغو");

        panel.add(saveButton);
        panel.add(cancelButton);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        final StudentRequest[] result = {null};

        saveButton.addActionListener(e -> {
            StudentRequest request = new StudentRequest(
                    nationalCodeField.getText().trim(),
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    classNameField.getText().trim()
            );

            // اعتبارسنجی
            var errors = ValidationDialog.validateStudent(request);
            if (!errors.isEmpty()) {
                ValidationDialog.showValidationErrors(dialog, errors);
                return;
            }

            result[0] = request;
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
        return result[0];
    }

    public static StudentRequest showEditDialog(Component parent, Student student) {
        if (student == null) return null;

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(parent),
                "ویرایش دانش‌آموز",
                true
        );

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel idLabel = new JLabel("شناسه: " + student.getId());
        JTextField nationalCodeField = new JTextField(student.getNationalCode());
        JTextField firstNameField = new JTextField(student.getFirstName());
        JTextField lastNameField = new JTextField(student.getLastName());
        JTextField classNameField = new JTextField(student.getClassName());

        panel.add(new JLabel("شناسه:"));
        panel.add(idLabel);
        panel.add(new JLabel("کد ملی:"));
        panel.add(nationalCodeField);
        panel.add(new JLabel("نام:"));
        panel.add(firstNameField);
        panel.add(new JLabel("نام خانوادگی:"));
        panel.add(lastNameField);
        panel.add(new JLabel("کلاس:"));
        panel.add(classNameField);

        JButton saveButton = new JButton("ذخیره تغییرات");
        JButton cancelButton = new JButton("لغو");

        panel.add(saveButton);
        panel.add(cancelButton);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        final StudentRequest[] result = {null};

        saveButton.addActionListener(e -> {
            StudentRequest request = new StudentRequest(
                    nationalCodeField.getText().trim(),
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    classNameField.getText().trim()
            );

            var errors = ValidationDialog.validateStudent(request);
            if (!errors.isEmpty()) {
                ValidationDialog.showValidationErrors(dialog, errors);
                return;
            }

            result[0] = request;
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
        return result[0];
    }

    public static void showDetailsDialog(Component parent, Student student) {
        if (student == null) return;

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(parent),
                "جزئیات دانش‌آموز",
                true
        );

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(createDetailLabel("شناسه:", String.valueOf(student.getId())));
        panel.add(createDetailLabel("کد ملی:", student.getNationalCode()));
        panel.add(createDetailLabel("نام:", student.getFirstName()));
        panel.add(createDetailLabel("نام خانوادگی:", student.getLastName()));
        panel.add(createDetailLabel("کلاس:", student.getClassName()));

        JButton closeButton = new JButton("بستن");
        panel.add(closeButton);

        closeButton.addActionListener(e -> dialog.dispose());

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static JPanel createDetailLabel(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(new JLabel("<html><b>" + label + "</b></html>"));
        panel.add(new JLabel(value));
        return panel;
    }
}