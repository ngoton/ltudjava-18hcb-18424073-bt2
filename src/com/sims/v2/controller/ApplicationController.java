package com.sims.v2.controller;

import com.sims.v2.model.*;
import com.sims.v2.service.*;
import com.sims.v2.view.ApplicationForm;
import com.sims.v2.view.AttendanceForm;

import java.util.Date;
import java.util.List;

public class ApplicationController {
    private ApplicationForm applicationForm;
    private ApplicationService applicationService;
    private TranscriptService transcriptService;
    private RemarkingService remarkingService;

    public ApplicationController(ApplicationForm applicationForm){
        this.applicationForm = applicationForm;
        this.applicationService = new ApplicationServiceImpl();
        this.transcriptService = new TranscriptServiceImpl();
        this.remarkingService = new RemarkingServiceImpl();
    }

    public List<Application> getList(){
        return applicationService.getList();
    }

    public List<Attendance> getTranscriptList(String code){
        return transcriptService.getListByStudent(code);
    }

    public List<Remarking> getRemarkingList(Date date){
        return remarkingService.getListByDate(date);
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

    public boolean deleteAll(String code){
        return applicationService.deleteAll(code);
    }

}
