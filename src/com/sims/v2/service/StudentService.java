package com.sims.v2.service;

import com.sims.v2.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getList();
    boolean create(Student student);
    boolean update(Student student);
    boolean delete(Student student);
    boolean deleteAll();
    List<Student> importFile(String path);
}
