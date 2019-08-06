package com.sims.v2.dao;

import com.sims.v2.model.Classes;

import java.util.List;

public interface ClassesDao {
    List<Classes> getList();
    Classes getClassById(Integer id);
    Classes getClassByName(String name);
    boolean addOne(Classes classes);
}
