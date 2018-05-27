package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.data.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 添加用户
     * @param user
     * @return
     */
    int add(User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    int update(User user);

    /**
     * 根据用户名和密码获取用户
     * @param account
     * @param password
     * @return
     */
    User getByAccountAndPassword(String account, String password);

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
     * 根据用户id获取用户
     * @param id
     * @return
     */
    User get(int id);

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
    User getByName(String name);
}
