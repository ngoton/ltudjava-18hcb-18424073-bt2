package com.sims.v2.dao;

import com.sims.v2.model.Attendance;
import com.sims.v2.model.Calendar;
import com.sims.v2.model.Student;

import java.util.List;

public interface AttendanceDao {
    List<Attendance> getList();
    Attendance getAttendanceById(Student student, Calendar calendar);
    List<Attendance> getAttendanceByCalendar(Calendar calendar);
    List<Attendance> getListByStudent(String code);
    boolean addOne(Attendance attendance);
    boolean updateOne(Attendance attendance);
    boolean deleteOne(Attendance attendance);
    boolean deleteAll();
    boolean updateAll(List<Attendance> attendances);
    List<Attendance> importFile(String path);
}
