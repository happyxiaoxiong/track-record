package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.data.entity.User;

import java.util.List;

public interface UserService {
    int add(User user);
    int update(User user);
    User getByAccountAndPassword(String account, String password);
    boolean existByAccount(String account);
    List<User> getAll();
    User get(int id);
    List<User> getAllByFields(List<String> strings);
    User getByName(String name);
}
