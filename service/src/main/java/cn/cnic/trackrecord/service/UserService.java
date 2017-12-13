package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.model.User;

public interface UserService {
    User getByAccountAndPassword(String account, String password);
}
