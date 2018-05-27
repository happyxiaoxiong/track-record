package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.data.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    /**
     * 添加用户
     * @param user
     * @return
     */
    int add(User user);

    /**
     * 根据用户名和密码获取用户
     * @param account
     * @param password
     * @return
     */
    User getByAccountAndPassword(@Param("account") String account, @Param("password") String password);

    /**
     * 根据用户名获取用户
     * @param account
     * @return
     */
    User getByAccount(@Param("account") String account);

    /**
     * 判断用户名是否存在
     * @param account
     * @return
     */
    boolean existByAccount(String account);

    /**
     * 获取所有用户
     * @return
     */
    List<User> getAll();

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    int update(User user);

    /**
     * 根据用户id获取用户
     * @param id
     * @return
     */
    User get(@Param("id") int id);

    /**
     * 获取所有用户指定字段的信息
     * @param fields
     * @return
     */
    List<User> getAllByFields(List<String> fields);

    /**
     * 根据用户姓名获取用户
     * @param name
     * @return
     */
    User getByName(@Param("name") String name);
}
