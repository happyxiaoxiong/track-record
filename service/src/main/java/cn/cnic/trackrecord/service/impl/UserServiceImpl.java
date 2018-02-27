package cn.cnic.trackrecord.service.impl;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.date.ShortDate;
import cn.cnic.trackrecord.common.enumeration.Gender;
import cn.cnic.trackrecord.common.enumeration.Role;
import cn.cnic.trackrecord.dao.UserDao;
import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public int add(User user) {
        user.setGender(ObjectUtils.defaultIfNull(user.getGender(), Gender.UNKNOWN));
        user.setBirthday(ObjectUtils.defaultIfNull(user.getBirthday(), ShortDate.NullValue));
        user.setEmail(ObjectUtils.defaultIfNull(user.getEmail(), ""));
        user.setOrganization(ObjectUtils.defaultIfNull(user.getOrganization(), ""));
        user.setCountry(ObjectUtils.defaultIfNull(user.getCountry(), ""));
        user.setProvince(ObjectUtils.defaultIfNull(user.getProvince(), ""));
        user.setCity(ObjectUtils.defaultIfNull(user.getCity(), ""));
        user.setCounty(ObjectUtils.defaultIfNull(user.getCounty(), ""));
        user.setTownship(ObjectUtils.defaultIfNull(user.getTownship(), ""));
        user.setAddTime(ObjectUtils.defaultIfNull(user.getAddTime(), LongDate.NullValue));
        user.setLoginTime(LongDate.NullValue);
        user.setRole(ObjectUtils.defaultIfNull(user.getRole(), Role.USER));
        return userDao.add(user);
    }

    @Override
    public int update(User user) {
        return userDao.update(user);
    }

    @Override
    public User getByAccountAndPassword(String account, String password) {
        return userDao.getByAccountAndPassword(account, password);
    }

    @Override
    public boolean existByAccount(String account) {
        return userDao.existByAccount(account);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User get(int id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAllByFields(List<String> fields) {
        return userDao.getAllByFields(fields);
    }
}
