package com.sims.v2.controller;

import com.sims.v2.model.Calendar;
import com.sims.v2.model.Classes;
import com.sims.v2.model.Subject;
import com.sims.v2.service.*;
import com.sims.v2.view.CalendarForm;

import java.util.List;

public class CalendarController {
    private CalendarForm calendarForm;
    private CalendarService calendarService;
    private ClassesService classesService;
    private SubjectService subjectService;

    public CalendarController(CalendarForm calendarForm){
        this.calendarForm = calendarForm;
        this.calendarService = new CalendarServiceImpl();
        this.classesService = new ClassesServiceImpl();
        this.subjectService = new SubjectServiceImpl();
    }

    public List<Calendar> getList(){
        return calendarService.getList();
    }

    public List<Classes> getClassList(){
        return classesService.getList();
    }

    public List<Subject> getSubjectList(){
        return subjectService.getList();
    }

    public boolean create(Calendar calendar){
        return calendarService.create(calendar);
    }

    public boolean update(Calendar calendar){
        return calendarService.update(calendar);
    }

    public boolean delete(Calendar calendar){
        return calendarService.delete(calendar);
    }

    public boolean deleteAll(){
        return calendarService.deleteAll();
    }

    public List<Calendar> importFile(String path){
        return calendarService.importFile(path);
    }
}
