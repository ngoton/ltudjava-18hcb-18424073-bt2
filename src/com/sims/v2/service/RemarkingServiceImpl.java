package com.sims.v2.service;

import com.sims.v2.dao.RemarkingDao;
import com.sims.v2.dao.RemarkingDaoImpl;
import com.sims.v2.model.Remarking;

import java.util.List;

public class RemarkingServiceImpl implements RemarkingService {
    private RemarkingDao remarkingDao;

    public RemarkingServiceImpl(){
        this.remarkingDao = new RemarkingDaoImpl();
    }

    @Override
    public List<Remarking> getList(){
        return remarkingDao.getList();
    }

    @Override
    public boolean create(Remarking remarking){
        return remarkingDao.addOne(remarking);
    }

    @Override
    public boolean update(Remarking remarking){
        return remarkingDao.updateOne(remarking);
    }

    @Override
    public boolean delete(Remarking remarking){
        return remarkingDao.deleteOne(remarking);
    }

    @Override
    public boolean deleteAll(){
        return remarkingDao.deleteAll();
    }

}
