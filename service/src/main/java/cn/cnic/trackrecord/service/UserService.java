package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.model.entity.User;

public interface UserService {
    int add(User user);

    User getByAccountAndPassword(String account, String password);
    boolean existByAccount(String account);
}
