package com.sims.v2.dao;

import com.sims.v2.model.Application;
import com.sims.v2.model.Attendance;
import com.sims.v2.model.Remarking;
import com.sims.v2.model.Student;

import java.util.List;

public interface ApplicationDao {
    List<Application> getList();
    List<Application> getListByStudent(String code);
    List<Application> getApplicationByRemarking(Remarking remarking);
    List<Application> getApplicationByAttendance(Attendance attendance);
    boolean addOne(Application application);
    boolean updateOne(Application application);
    boolean deleteOne(Application application);
    boolean deleteAllByStudent(String code);
    boolean deleteAll();
    boolean updateAll(List<Application> applications);

}
