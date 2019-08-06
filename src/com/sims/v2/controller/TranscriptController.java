package com.sims.v2.controller;

import com.sims.v2.model.*;
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

    public List<Transcript> getList(){
        return transcriptService.getList();
    }

    public List<Transcript> getListByStudent(String code){
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

    public boolean save(List<Transcript> transcripts){
        return transcriptService.save(transcripts);
    }

    public boolean deleteAll(){
        return transcriptService.deleteAll();
    }

    public List<Transcript> importFile(String path){
        return transcriptService.importFile(path);
    }
}
