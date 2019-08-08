package com.sims.v2.service;

import com.sims.v2.dao.AttendanceDao;
import com.sims.v2.dao.AttendanceDaoImpl;
import com.sims.v2.model.Attendance;

import java.util.List;

public class TranscriptServiceImpl implements TranscriptService {
    private AttendanceDao attendanceDao;

    public TranscriptServiceImpl(){
        this.attendanceDao = new AttendanceDaoImpl();
    }

    @Override
    public List<Attendance> getList(){
        return attendanceDao.getList();
    }

    @Override
    public List<Attendance> getListByStudent(String code){
        return attendanceDao.getListByStudent(code);
    }

    @Override
    public boolean create(Attendance attendance){
        return attendanceDao.addOne(attendance);
    }

    @Override
    public boolean update(Attendance attendance){
        return attendanceDao.updateOne(attendance);
    }

    @Override
    public boolean delete(Attendance attendance){
        return attendanceDao.deleteOne(attendance);
    }

    @Override
    public boolean deleteAll(){
        return attendanceDao.deleteAll();
    }

    @Override
    public List<Attendance> importFile(String path){
        return attendanceDao.importFile(path);
    }

}
