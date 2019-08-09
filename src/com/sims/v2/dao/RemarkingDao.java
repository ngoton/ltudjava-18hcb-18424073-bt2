package com.sims.v2.dao;

import com.sims.v2.model.Remarking;

import java.util.List;

public interface RemarkingDao {
    List<Remarking> getList();
    Remarking getRemarkingById(String id);
    boolean addOne(Remarking remarking);
    boolean updateOne(Remarking remarking);
    boolean deleteOne(Remarking remarking);
    boolean deleteAll();
}
