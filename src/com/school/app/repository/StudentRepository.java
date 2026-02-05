package com.school.app.repository;

import com.school.app.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    List<Student> findAll();

    Optional<Student> findById(long id);

    Optional<Student> findByNationalCode(String nationalCode);

    Student save(Student student);

    Student update(long id, Student student);

    boolean delete(long id);
}
