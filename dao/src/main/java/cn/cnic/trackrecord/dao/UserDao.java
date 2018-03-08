package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.data.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    int add(User user);

    User getByAccountAndPassword(@Param("account") String account,@Param("password") String password);
    User getByAccount(@Param("account") String account);
    boolean existByAccount(String account);

    List<User> getAll();

    int update(User user);

    User get(@Param("id") int id);

    List<User> getAllByFields(List<String> fields);

    User getByName(@Param("name") String name);
}
