package com.sims.v2.service;

import com.sims.v2.model.Attendance;

import java.util.List;

public interface TranscriptService {
    List<Attendance> getList();
    List<Attendance> getListByStudent(String code);
    boolean create(Attendance attendance);
    boolean update(Attendance attendance);
    boolean delete(Attendance attendance);
    boolean deleteAll();
    List<Attendance> importFile(String path);
}
