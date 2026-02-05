package com.school.app.dialog;

import com.school.app.dto.StudentRequest;
import com.school.app.dto.UserRequest;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ValidationDialog {

    public static List<String> validateStudent(StudentRequest request) {
        List<String> errors = new ArrayList<>();

        if (request == null) {
            errors.add("اطلاعات دانش‌آموز الزامی است");
            return errors;
        }

        if (request.getNationalCode() == null || request.getNationalCode().trim().isEmpty()) {
            errors.add("کد ملی الزامی است");
        } else if (request.getNationalCode().length() != 10) {
            errors.add("کد ملی باید ۱۰ رقمی باشد");
        } else if (!request.getNationalCode().matches("\\d+")) {
            errors.add("کد ملی باید فقط عدد باشد");
        }

        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            errors.add("نام الزامی است");
        } else if (request.getFirstName().length() < 2 || request.getFirstName().length() > 50) {
            errors.add("نام باید بین ۲ تا ۵۰ کاراکتر باشد");
        }

        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            errors.add("نام خانوادگی الزامی است");
        } else if (request.getLastName().length() < 2 || request.getLastName().length() > 50) {
            errors.add("نام خانوادگی باید بین ۲ تا ۵۰ کاراکتر باشد");
        }

        if (request.getClassName() == null || request.getClassName().trim().isEmpty()) {
            errors.add("کلاس الزامی است");
        } else if (request.getClassName().length() < 2 || request.getClassName().length() > 20) {
            errors.add("نام کلاس باید بین ۲ تا ۲۰ کاراکتر باشد");
        }

        return errors;
    }

    public static void showValidationErrors(Component parent, List<String> errors) {
        if (errors == null || errors.isEmpty()) return;

        StringBuilder message = new StringBuilder("لطفا خطاهای زیر را اصلاح کنید:\n\n");
        for (int i = 0; i < errors.size(); i++) {
            message.append(i + 1).append(". ").append(errors.get(i)).append("\n");
        }

        MessageDialog.showError(parent, message.toString());
    }

    public static List<String> validateUser(UserRequest request) {
        List<String> errors = new ArrayList<>();

        if (request == null) {
            errors.add("اطلاعات کاربر الزامی است");
            return errors;
        }

        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            errors.add("نام کامل الزامی است");
        } else if (request.getFullName().length() < 3 || request.getFullName().length() > 100) {
            errors.add("نام کامل باید بین ۳ تا ۱۰۰ کاراکتر باشد");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            errors.add("نام کاربری الزامی است");
        } else if (request.getUsername().length() < 3 || request.getUsername().length() > 50) {
            errors.add("نام کاربری باید بین ۳ تا ۵۰ کاراکتر باشد");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            errors.add("رمز عبور الزامی است");
        } else if (request.getPassword().length() < 4 || request.getPassword().length() > 100) {
            errors.add("رمز عبور باید بین ۴ تا ۱۰۰ کاراکتر باشد");
        }

        return errors;
    }

    public static List<String> validateLogin(String username, String password) {
        List<String> errors = new ArrayList<>();

        if (username == null || username.trim().isEmpty()) {
            errors.add("نام کاربری الزامی است");
        }

        if (password == null || password.isEmpty()) {
            errors.add("رمز عبور الزامی است");
        }

        return errors;
    }
}