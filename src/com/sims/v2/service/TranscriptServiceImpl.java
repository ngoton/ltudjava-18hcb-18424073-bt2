package com.sims.v2.service;

import java.util.List;

public class TranscriptServiceImpl implements TranscriptService {
    private TranscriptDao transcriptDao;

    public TranscriptServiceImpl(){
        this.transcriptDao = new TranscriptDaoImpl();
    }

    @Override
    public List<Transcript> getList(){
        return transcriptDao.getList();
    }

    @Override
    public List<Transcript> getListByStudent(String code){
        return transcriptDao.getListByStudent(code);
    }

    @Override
    public boolean save(List<Transcript> transcripts){
        return transcriptDao.save(transcripts);
    }

    @Override
    public boolean deleteAll(){
        return transcriptDao.deleteAll();
    }

    @Override
    public List<Transcript> importFile(String path){
        return transcriptDao.importFile(path);
    }

}
