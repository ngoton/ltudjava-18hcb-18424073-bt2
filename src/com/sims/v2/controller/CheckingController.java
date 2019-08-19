package com.sims.v2.controller;

import com.sims.v2.model.Application;
import com.sims.v2.model.Attendance;
import com.sims.v2.model.Remarking;
import com.sims.v2.service.*;

import java.util.Date;
import java.util.List;

public class CheckingController {
    private ApplicationService applicationService;
    private TranscriptService transcriptService;
    private RemarkingService remarkingService;

    public CheckingController(){
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

    public boolean deleteAll(){
        return applicationService.deleteAll();
    }

}
