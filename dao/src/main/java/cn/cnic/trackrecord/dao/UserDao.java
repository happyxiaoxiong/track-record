package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    User getByAccountAndPassword(@Param("account") String account,@Param("password") String password);
}
