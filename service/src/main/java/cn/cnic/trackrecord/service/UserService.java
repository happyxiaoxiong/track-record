package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.data.entity.User;

public interface UserService {
    int add(User user);

    User getByAccountAndPassword(String account, String password);
    boolean existByAccount(String account);
}
