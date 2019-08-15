package com.sims.v2.service;

import com.sims.v2.model.Application;

import java.util.List;

public interface ApplicationService {
    List<Application> getList();
    boolean create(Application application);
    boolean update(Application application);
    boolean delete(Application application);
    boolean deleteAll(String code);
}
