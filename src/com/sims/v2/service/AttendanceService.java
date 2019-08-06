package com.sims.v2.service;

import com.sims.v2.model.Attendance;

import java.util.List;

public interface AttendanceService {
    List<Attendance> getList();
    boolean save(List<Attendance> attendances);
    boolean addOne(Attendance attendance);
    boolean deleteAll();
}
