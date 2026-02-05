package com.school.app.service;

import com.school.app.model.Response;
import com.school.app.model.Student;

import java.util.List;

public interface StudentService {
    Response<List<Student>> list();
    Response<Student> add(Student s);
    Response<Student> update(long id, Student s);
    Response<Boolean> delete(long id);
    Response<Student> findById(long id);
}
