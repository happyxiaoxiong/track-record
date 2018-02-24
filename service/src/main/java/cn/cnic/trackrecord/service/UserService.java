package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.data.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    int add(User user);
    int update(User user);
    User getByAccountAndPassword(String account, String password);
    boolean existByAccount(String account);
    List<User> getAll();
    User get(int id);
}
