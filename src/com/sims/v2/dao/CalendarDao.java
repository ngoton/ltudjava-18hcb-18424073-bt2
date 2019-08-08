package com.sims.v2.dao;

import com.sims.v2.model.Calendar;
import com.sims.v2.model.Classes;
import com.sims.v2.model.Subject;

import java.util.List;

public interface CalendarDao {
    List<Calendar> getList();
    List<Calendar> getListByStudent(String code);
    Calendar getCalendarById(Classes classes, Subject subject);
    Calendar getCalendarByName(String name);
    boolean addOne(Calendar calendar);
    boolean updateOne(Calendar calendar);
    boolean deleteOne(Calendar calendar);
    boolean deleteAll();
    List<Calendar> importFile(String path);
}
