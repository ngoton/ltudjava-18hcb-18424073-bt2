package com.sims.v2.dao;

import com.sims.v2.model.Attendance;
import com.sims.v2.model.Calendar;

import java.util.List;

public interface AttendanceDao {
    List<Attendance> getList();
    Attendance getAttendanceById(Integer id);
    List<Attendance> getAttendanceByCalendar(Calendar calendar);
    boolean save(List<Attendance> attendances);
    boolean addOne(Attendance attendances);
    boolean deleteAll();
    boolean updateAll(List<Attendance> attendances);
}
