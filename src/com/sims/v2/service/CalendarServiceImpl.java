package com.sims.v2.service;

import com.sims.v2.dao.CalendarDao;
import com.sims.v2.dao.CalendarDaoImpl;
import com.sims.v2.model.Calendar;

import java.util.List;

public class CalendarServiceImpl implements CalendarService {
    private CalendarDao calendarDao;

    public CalendarServiceImpl(){
        this.calendarDao = new CalendarDaoImpl();
    }

    @Override
    public List<Calendar> getList(){
        return calendarDao.getList();
    }

    @Override
    public List<Calendar> getListByStudent(String code){
        return calendarDao.getListByStudent(code);
    }

    @Override
    public boolean create(Calendar calendar){
        return calendarDao.addOne(calendar);
    }

    @Override
    public boolean update(Calendar calendar){
        return calendarDao.updateOne(calendar);
    }

    @Override
    public boolean delete(Calendar calendar){
        return calendarDao.deleteOne(calendar);
    }

    @Override
    public boolean deleteAll(){
        return calendarDao.deleteAll();
    }

    @Override
    public List<Calendar> importFile(String path){
        return calendarDao.importFile(path);
    }
}
