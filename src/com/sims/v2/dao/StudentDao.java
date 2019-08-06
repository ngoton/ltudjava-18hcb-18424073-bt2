package com.sims.v2.dao;

import com.sims.v2.model.Student;

import java.util.List;

public interface StudentDao {
    List<Student> getList();
    Student getStudentById(Integer id);
    Student getStudentByCode(String code);
    boolean save(List<Student> students);
    boolean deleteAll();
    List<Student> importFile(String path);
}
