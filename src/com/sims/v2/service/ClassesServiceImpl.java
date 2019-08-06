package com.sims.v2.service;

import com.sims.v2.dao.ClassesDao;
import com.sims.v2.dao.ClassesDaoImpl;
import com.sims.v2.model.Classes;

import java.util.List;

public class ClassesServiceImpl implements ClassesService {
    private ClassesDao classesDao;

    public ClassesServiceImpl(){
        this.classesDao = new ClassesDaoImpl();
    }
    @Override
    public List<Classes> getList(){
        return classesDao.getList();
    }
}
