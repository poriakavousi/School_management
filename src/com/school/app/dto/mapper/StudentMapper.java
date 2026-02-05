package com.school.app.dto.mapper;

import com.school.app.dto.StudentRequest;
import com.school.app.dto.StudentResponse;
import com.school.app.model.Student;

import java.time.LocalDateTime;

public class StudentMapper {

    public static StudentResponse toResponse(Student student) {
        if (student == null) return null;

        return new StudentResponse(
                student.getId(),
                student.getNationalCode(),
                student.getFirstName(),
                student.getLastName(),
                student.getClassName(),
                LocalDateTime.now() //
        );
    }

    public static Student toEntity(StudentRequest request) {
        if (request == null) return null;

        Student student = new Student();
        student.setNationalCode(request.getNationalCode());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setClassName(request.getClassName());
        return student;
    }

    public static Student updateEntity(Student student, StudentRequest request) {
        if (student == null || request == null) return student;

        student.setNationalCode(request.getNationalCode());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setClassName(request.getClassName());
        return student;
    }
}