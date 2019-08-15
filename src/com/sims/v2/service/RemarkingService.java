package com.sims.v2.service;

import com.sims.v2.model.Remarking;

import java.util.Date;
import java.util.List;

public interface RemarkingService {
    List<Remarking> getList();
    List<Remarking> getListByDate(Date date);
    boolean create(Remarking remarking);
    boolean update(Remarking remarking);
    boolean delete(Remarking remarking);
    boolean deleteAll();
}
