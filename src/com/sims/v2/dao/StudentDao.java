package com.sims.v2.dao;

import com.sims.v2.model.Classes;
import com.sims.v2.model.Student;

import java.util.List;

public interface StudentDao {
    List<Student> getList();
    Student getStudentById(Integer id);
    Student getStudentByCode(String code);
    List<Student> getStudentByClass(Classes classes);
    boolean addOne(Student student);
    boolean updateOne(Student student);
    boolean deleteOne(Student student);
    boolean deleteAll();
    List<Student> importFile(String path);
}
