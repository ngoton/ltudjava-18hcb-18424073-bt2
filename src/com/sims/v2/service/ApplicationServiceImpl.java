package com.sims.v2.service;

import com.sims.v2.dao.ApplicationDao;
import com.sims.v2.dao.ApplicationDaoImpl;
import com.sims.v2.model.Application;
import com.sims.v2.model.Student;

import java.util.List;

public class ApplicationServiceImpl implements ApplicationService {
    private ApplicationDao applicationDao;

    public ApplicationServiceImpl(){
        this.applicationDao = new ApplicationDaoImpl();
    }

    @Override
    public List<Application> getList(){
        return applicationDao.getList();
    }

    @Override
    public boolean create(Application application){
        return applicationDao.addOne(application);
    }

    @Override
    public boolean update(Application application){
        return applicationDao.updateOne(application);
    }

    @Override
    public boolean delete(Application application){
        return applicationDao.deleteOne(application);
    }

    @Override
    public boolean deleteAll(String code){
        return applicationDao.deleteAll(code);
    }

}
