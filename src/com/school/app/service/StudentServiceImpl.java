package com.school.app.service;

import com.school.app.dto.StudentRequest;
import com.school.app.dto.mapper.StudentMapper;
import com.school.app.dialog.MessageDialog;
import com.school.app.model.Response;
import com.school.app.model.Student;
import com.school.app.repository.StudentRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;
    private Component parentComponent;

    public StudentServiceImpl(StudentRepository repo) {
        this.repo = repo;
    }

    public void setParentComponent(Component parent) {
        this.parentComponent = parent;
    }

    @Override
    public Response<List<Student>> list() {
        try {
            List<Student> students = repo.findAll();
            return Response.ok("لیست دانش‌آموزان دریافت شد", students);
        } catch (Exception e) {
            showErrorDialog("خطا در دریافت لیست دانش‌آموزان: " + e.getMessage());
            return Response.error(500, "خطای سرور: " + e.getMessage());
        }
    }

    @Override
    public Response<Student> add(Student s) {
        try {
            StudentRequest request = new StudentRequest(
                    s.getNationalCode(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getClassName()
            );

            // اعتبارسنجی
            String validationError = validateStudentRequest(request);
            if (validationError != null) {
                return Response.error(400, validationError);
            }

            Optional<Student> exists = repo.findByNationalCode(s.getNationalCode());
            if (exists.isPresent()) {
                return Response.error(400, "کد ملی باید یکتا باشد");
            }

            Student created = repo.save(s);
            showSuccessDialog("دانش‌آموز با موفقیت ایجاد شد");
            return Response.created("دانش‌آموز با موفقیت ایجاد شد", created);
        } catch (Exception e) {
            showErrorDialog("خطا در ایجاد دانش‌آموز: " + e.getMessage());
            return Response.error(500, "خطا در ایجاد: " + e.getMessage());
        }
    }

    @Override
    public Response<Student> update(long id, Student s) {
        try {
            StudentRequest request = new StudentRequest(
                    s.getNationalCode(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getClassName()
            );

            String validationError = validateStudentRequest(request);
            if (validationError != null) {
                return Response.error(400, validationError);
            }

            if (repo.findById(id).isEmpty()) {
                return Response.error(404, "دانش‌آموز یافت نشد");
            }

            repo.findByNationalCode(s.getNationalCode()).ifPresent(found -> {
                if (!found.getId().equals(id)) {
                    throw new RuntimeException("کد ملی باید یکتا باشد");
                }
            });

            Student updated = repo.update(id, s);
            showSuccessDialog("دانش‌آموز با موفقیت بروزرسانی شد");
            return Response.ok("دانش‌آموز با موفقیت بروزرسانی شد", updated);
        } catch (Exception e) {
            if ("کد ملی باید یکتا باشد".equalsIgnoreCase(e.getMessage())) {
                return Response.error(400, e.getMessage());
            }
            showErrorDialog("خطا در بروزرسانی دانش‌آموز: " + e.getMessage());
            return Response.error(500, "خطا در بروزرسانی: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> delete(long id) {
        try {
            boolean ok = repo.delete(id);
            if (!ok) {
                return Response.error(404, "دانش‌آموز یافت نشد");
            }
            showSuccessDialog("دانش‌آموز با موفقیت حذف شد");
            return Response.ok("دانش‌آموز با موفقیت حذف شد", true);
        } catch (Exception e) {
            showErrorDialog("خطا در حذف دانش‌آموز: " + e.getMessage());
            return Response.error(500, "خطا در حذف: " + e.getMessage());
        }
    }

    @Override
    public Response<Student> findById(long id) {
        try {
            return repo.findById(id)
                    .map(s -> Response.ok("دانش‌آموز یافت شد", s))
                    .orElse(Response.error(404, "دانش‌آموز یافت نشد"));
        } catch (Exception e) {
            showErrorDialog("خطا در جستجوی دانش‌آموز: " + e.getMessage());
            return Response.error(500, "خطا در جستجو: " + e.getMessage());
        }
    }

    private String validateStudentRequest(StudentRequest request) {
        if (request == null) return "اطلاعات دانش‌آموز الزامی است";
        if (isBlank(request.getNationalCode())) return "کد ملی الزامی است";
        if (isBlank(request.getFirstName())) return "نام الزامی است";
        if (isBlank(request.getLastName())) return "نام خانوادگی الزامی است";
        if (isBlank(request.getClassName())) return "کلاس الزامی است";

        if (request.getNationalCode().length() != 10) {
            return "کد ملی باید ۱۰ رقمی باشد";
        }
        if (!request.getNationalCode().matches("\\d+")) {
            return "کد ملی باید فقط عدد باشد";
        }
        if (request.getFirstName().length() < 2 || request.getFirstName().length() > 50) {
            return "نام باید بین ۲ تا ۵۰ کاراکتر باشد";
        }
        if (request.getLastName().length() < 2 || request.getLastName().length() > 50) {
            return "نام خانوادگی باید بین ۲ تا ۵۰ کاراکتر باشد";
        }
        if (request.getClassName().length() < 2 || request.getClassName().length() > 20) {
            return "نام کلاس باید بین ۲ تا ۲۰ کاراکتر باشد";
        }

        return null;
    }

    private boolean isBlank(String x) {
        return x == null || x.trim().isEmpty();
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