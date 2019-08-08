package com.sims.v2.service;

import com.sims.v2.model.Calendar;

import java.util.List;

public interface CalendarService {
    List<Calendar> getList();
    List<Calendar> getListByStudent(String code);
    boolean create(Calendar calendar);
    boolean update(Calendar calendar);
    boolean delete(Calendar calendar);
    boolean deleteAll();
    List<Calendar> importFile(String path);
}
