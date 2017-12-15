package cn.cnic.trackrecord.api.identity;

import cn.cnic.trackrecord.dao.UserDao;
import cn.cnic.trackrecord.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userDao.getByAccount(account);
        return new TokenUser(user);
    }
}
