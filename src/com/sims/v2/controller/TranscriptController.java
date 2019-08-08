package com.sims.v2.controller;

import com.sims.v2.model.Attendance;
import com.sims.v2.model.Calendar;
import com.sims.v2.model.Student;
import com.sims.v2.service.*;

import java.util.List;

public class TranscriptController<T> {
    private T transcriptForm;
    private TranscriptService transcriptService;
    private CalendarService calendarService;
    private StudentService studentService;

    public TranscriptController(T transcriptForm){
        this.transcriptForm = transcriptForm;
        this.transcriptService = new TranscriptServiceImpl();
        this.calendarService = new CalendarServiceImpl();
        this.studentService = new StudentServiceImpl();
    }

    public List<Attendance> getList(){
        return transcriptService.getList();
    }

    public List<Attendance> getListByStudent(String code){
        return transcriptService.getListByStudent(code);
    }

    public List<Calendar> getCalendarList(){
        return calendarService.getList();
    }

    public List<Calendar> getCalendarListByStudent(String code){
        return calendarService.getListByStudent(code);
    }

    public List<Student> getStudentList(){
        return studentService.getList();
    }

    public boolean create(Attendance attendance){
        return transcriptService.create(attendance);
    }

    public boolean update(Attendance attendance){
        return transcriptService.update(attendance);
    }

    public boolean delete(Attendance attendance){
        return transcriptService.delete(attendance);
    }

    public boolean deleteAll(){
        return transcriptService.deleteAll();
    }

    public List<Attendance> importFile(String path){
        return transcriptService.importFile(path);
    }
}
