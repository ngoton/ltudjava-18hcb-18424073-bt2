package com.sims.v2.controller;

import com.sims.v2.model.*;
import com.sims.v2.service.*;
import com.sims.v2.view.ApplicationForm;
import com.sims.v2.view.AttendanceForm;

import java.util.Date;
import java.util.List;

public class ApplicationController {
    private ApplicationService applicationService;
    private TranscriptService transcriptService;
    private RemarkingService remarkingService;
    private CalendarService calendarService;

    public ApplicationController(){
        this.applicationService = new ApplicationServiceImpl();
        this.transcriptService = new TranscriptServiceImpl();
        this.remarkingService = new RemarkingServiceImpl();
        this.calendarService = new CalendarServiceImpl();
    }

    public List<Application> getList(){
        return applicationService.getList();
    }

    public List<Application> getListByStudent(String code){
        return applicationService.getListByStudent(code);
    }

    public List<Attendance> getTranscriptList(String code){
        return transcriptService.getListByStudent(code);
    }

    public List<Remarking> getRemarkingList(Date date){
        return remarkingService.getListByDate(date);
    }

    public List<Calendar> getCalendarList(){
        return calendarService.getList();
    }

    public boolean create(Application application){
        return applicationService.create(application);
    }

    public boolean update(Application application){
        return applicationService.update(application);
    }

    public boolean delete(Application application){
        return applicationService.delete(application);
    }

    public boolean deleteAllByStudent(String code){
        return applicationService.deleteAllByStudent(code);
    }

}
