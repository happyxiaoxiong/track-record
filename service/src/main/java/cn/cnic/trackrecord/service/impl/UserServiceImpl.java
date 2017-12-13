package cn.cnic.trackrecord.service.impl;

import cn.cnic.trackrecord.dao.UserDao;
import cn.cnic.trackrecord.model.User;
import cn.cnic.trackrecord.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getByAccountAndPassword(String account, String password) {
        return null;
    }
}
