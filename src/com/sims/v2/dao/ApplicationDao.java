package com.sims.v2.dao;

import com.sims.v2.model.Application;
import com.sims.v2.model.Student;

import java.util.List;

public interface ApplicationDao {
    List<Application> getList();
    List<Application> getListByStudent(String code);
    boolean addOne(Application application);
    boolean updateOne(Application application);
    boolean deleteOne(Application application);
    boolean deleteAll(String code);
    boolean updateAll(List<Application> applications);
}
