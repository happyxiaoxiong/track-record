package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.data.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    int add(User user);

    User getByAccountAndPassword(@Param("account") String account,@Param("password") String password);
    User getByAccount(@Param("account") String account);
    boolean existByAccount(String account);
}
