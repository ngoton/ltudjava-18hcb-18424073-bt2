package com.sims.v2.service;

import com.sims.v2.dao.UserDao;
import com.sims.v2.dao.UserDaoImpl;
import com.sims.v2.model.User;

public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserServiceImpl(){
        this.userDao = new UserDaoImpl();
    }

    public User login(String username, String password){
        return userDao.login(username, password);
    }

    public boolean changePassword(User user){
        return userDao.changePassword(user);
    }
}
