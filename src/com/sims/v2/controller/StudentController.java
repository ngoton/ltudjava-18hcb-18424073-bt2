package com.sims.v2.controller;

import com.sims.v2.model.Classes;
import com.sims.v2.model.Student;
import com.sims.v2.service.ClassesService;
import com.sims.v2.service.ClassesServiceImpl;
import com.sims.v2.service.StudentService;
import com.sims.v2.service.StudentServiceImpl;
import com.sims.v2.view.StudentForm;

import java.util.List;

public class StudentController {
    private StudentForm studentForm;
    private StudentService studentService;
    private ClassesService classesService;

    public StudentController(StudentForm studentForm){
        this.studentForm = studentForm;
        this.studentService = new StudentServiceImpl();
        this.classesService = new ClassesServiceImpl();
    }

    public List<Student> getList(){
        return studentService.getList();
    }

    public List<Classes> getClassList(){
        return classesService.getList();
    }

    public boolean create(Student student){
        return studentService.create(student);
    }

    public boolean update(Student student){
        return studentService.update(student);
    }

    public boolean delete(Student student){
        return studentService.delete(student);
    }

    public boolean deleteAll(){
        return studentService.deleteAll();
    }

    public List<Student> importFile(String path){
        return studentService.importFile(path);
    }
}
