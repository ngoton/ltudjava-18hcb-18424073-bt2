package com.sims.v2.service;

import com.sims.v2.model.Attendance;

import java.util.List;

public interface AttendanceService {
    List<Attendance> getList();
    boolean create(Attendance attendance);
    boolean update(Attendance attendance);
    boolean delete(Attendance attendance);
    boolean deleteAll();
}
