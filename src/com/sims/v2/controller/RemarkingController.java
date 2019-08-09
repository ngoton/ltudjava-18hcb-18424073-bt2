package com.sims.v2.controller;

import com.sims.v2.model.*;
import com.sims.v2.service.RemarkingService;
import com.sims.v2.service.RemarkingServiceImpl;
import com.sims.v2.view.RemarkingForm;

import java.util.List;

public class RemarkingController {
    private RemarkingForm remarkingForm;
    private RemarkingService remarkingService;

    public RemarkingController(RemarkingForm remarkingForm){
        this.remarkingForm = remarkingForm;
        this.remarkingService = new RemarkingServiceImpl();
    }

    public List<Remarking> getList(){
        return remarkingService.getList();
    }

    public boolean create(Remarking remarking){
        return remarkingService.create(remarking);
    }

    public boolean update(Remarking remarking){
        return remarkingService.update(remarking);
    }

    public boolean delete(Remarking remarking){
        return remarkingService.delete(remarking);
    }

    public boolean deleteAll(){
        return remarkingService.deleteAll();
    }

}
