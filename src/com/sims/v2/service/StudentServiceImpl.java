package com.sims.v2.service;

import com.sims.v2.dao.StudentDao;
import com.sims.v2.dao.StudentDaoImpl;
import com.sims.v2.model.Student;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private StudentDao studentDao;

    public StudentServiceImpl(){
        this.studentDao = new StudentDaoImpl();
    }

    @Override
    public List<Student> getList(){
        return studentDao.getList();
    }

    @Override
    public boolean save(List<Student> students){
        return studentDao.save(students);
    }

    @Override
    public boolean deleteAll(){
        return studentDao.deleteAll();
    }

    @Override
    public List<Student> importFile(String path){
        return studentDao.importFile(path);
    }
}
