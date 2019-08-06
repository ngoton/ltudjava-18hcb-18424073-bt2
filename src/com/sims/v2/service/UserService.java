package com.sims.v2.service;

import com.sims.v2.model.User;

public interface UserService {
    User login(String username, String password);
    boolean changePassword(User user);
}
