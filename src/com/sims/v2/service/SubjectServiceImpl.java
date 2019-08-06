package com.sims.v2.service;

import com.sims.v2.dao.SubjectDao;
import com.sims.v2.dao.SubjectDaoImpl;
import com.sims.v2.model.Subject;

import java.util.List;

public class SubjectServiceImpl implements SubjectService {
    private SubjectDao subjectDao;

    public SubjectServiceImpl(){
        this.subjectDao = new SubjectDaoImpl();
    }
    @Override
    public List<Subject> getList(){
        return subjectDao.getList();
    }
}
