package com.sims.v2.service;

import java.util.List;

public interface TranscriptService {
    List<Transcript> getList();
    List<Transcript> getListByStudent(String code);
    boolean save(List<Transcript> transcripts);
    boolean deleteAll();
    List<Transcript> importFile(String path);
}
