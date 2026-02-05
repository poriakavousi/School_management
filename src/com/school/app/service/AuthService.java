package com.school.app.service;

import com.school.app.dialog.MessageDialog;
import com.school.app.model.Response;
import com.school.app.model.User;
import com.school.app.repository.UserRepository;

import java.awt.*;  // اضافه کردن import برای Component
import java.util.Optional;

public class AuthService {
    private final UserRepository userRepo;
    private Component parentComponent;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void setParentComponent(Component parent) {
        this.parentComponent = parent;
    }

    public Response<User> login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return Response.error(400, "نام کاربری الزامی است");
        }
        if (password == null || password.isEmpty()) {
            return Response.error(400, "رمز عبور الزامی است");
        }

        try {
            Optional<User> found = userRepo.findByUsername(username);
            if (found.isEmpty()) {
                showErrorDialog("نام کاربری یافت نشد");
                return Response.error(401, "نام کاربری یافت نشد");
            }

            User user = found.get();
            if (!user.getPassword().equals(password)) {
                showErrorDialog("رمز عبور نادرست است");
                return Response.error(401, "رمز عبور نادرست است");
            }

            showSuccessDialog("خوش آمدید " + user.getFullName() + "!");
            return Response.ok("ورود موفقیت‌آمیز بود", user);
        } catch (Exception e) {
            showErrorDialog("خطا در ورود: " + e.getMessage());
            return Response.error(500, "خطا در ورود: " + e.getMessage());
        }
    }

    public Response<User> register(String username, String password, String fullName) {
        if (username == null || username.trim().isEmpty()) {
            return Response.error(400, "نام کاربری الزامی است");
        }
        if (password == null || password.isEmpty()) {
            return Response.error(400, "رمز عبور الزامی است");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return Response.error(400, "نام کامل الزامی است");
        }

        try {
            if (userRepo.existsByUsername(username)) {
                showErrorDialog("نام کاربری قبلاً ثبت شده است");
                return Response.error(400, "نام کاربری قبلاً ثبت شده است");
            }

            if (username.length() < 3 || username.length() > 50) {
                return Response.error(400, "نام کاربری باید بین ۳ تا ۵۰ کاراکتر باشد");
            }
            if (password.length() < 4) {
                return Response.error(400, "رمز عبور باید حداقل ۴ کاراکتر باشد");
            }
            if (fullName.length() < 3 || fullName.length() > 100) {
                return Response.error(400, "نام کامل باید بین ۳ تا ۱۰۰ کاراکتر باشد");
            }

            User newUser = new User(null, username, password, fullName);
            User saved = userRepo.save(newUser);

            showSuccessDialog("ثبت‌نام با موفقیت انجام شد");
            return Response.created("ثبت‌نام موفقیت‌آمیز بود", saved);
        } catch (Exception e) {
            showErrorDialog("خطا در ثبت‌نام: " + e.getMessage());
            return Response.error(500, "خطا در ثبت‌نام: " + e.getMessage());
        }
    }

    private void showSuccessDialog(String message) {
        if (parentComponent != null) {
            MessageDialog.showSuccess(parentComponent, message);
        }
    }

    private void showErrorDialog(String message) {
        if (parentComponent != null) {
            MessageDialog.showError(parentComponent, message);
        }
    }

    private void showWarningDialog(String message) {
        if (parentComponent != null) {
            MessageDialog.showWarning(parentComponent, message);
        }
    }
}