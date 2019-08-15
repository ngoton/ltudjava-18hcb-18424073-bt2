package com.sims.v2.dao;

import com.sims.v2.model.Remarking;

import java.util.Date;
import java.util.List;

public interface RemarkingDao {
    List<Remarking> getList();
    List<Remarking> getListByDate(Date date);
    Remarking getRemarkingById(String id);
    boolean addOne(Remarking remarking);
    boolean updateOne(Remarking remarking);
    boolean deleteOne(Remarking remarking);
    boolean deleteAll();
}
