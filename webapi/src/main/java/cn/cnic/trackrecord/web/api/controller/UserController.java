package cn.cnic.trackrecord.web.api.controller;

import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.data.vo.AuthUser;
import cn.cnic.trackrecord.data.vo.UserChangePassword;
import cn.cnic.trackrecord.service.UserService;
import cn.cnic.trackrecord.web.Const;
import cn.cnic.trackrecord.web.identity.TokenUtils;
import cn.cnic.trackrecord.web.identity.UserDetailsServiceImpl;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "用户API", description = "用户API", tags = "User")
@RestController
@RequestMapping(value = Const.API_ROOT + "user")
public class UserController {

    @Autowired
    private TokenUtils tokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "用户修改密码")
    @RequestMapping(value = "change/password", method = RequestMethod.POST)
    public HttpRes<AuthUser> changePassword(@RequestBody UserChangePassword userChangePassword) {
        User user = userService.get(userDetailsService.getLoginUser().getId());
        if (passwordEncoder.matches(userChangePassword.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userChangePassword.getNewPassword()));
            if (userService.update(user) > 0) {
                return HttpRes.success(getAuthUser(user));
            }
        }
        return HttpRes.fail();
    }

    @ApiOperation(value = "用户修改密码")
    @RequestMapping(value = "change/profile", method = RequestMethod.POST)
    public HttpRes<?> changeInfo(@RequestBody User user) {
        user.setId(userDetailsService.getLoginUser().getId());
        if (userService.update(user) > 0) {
            return HttpRes.success(getAuthUser(userService.get(user.getId())));
        }
        return HttpRes.fail();
    }

    private AuthUser getAuthUser(User user) {
        String token = tokenUtil.createTokenForUser(user);
        user.setPassword(null);
        return new AuthUser(token, user);
    }

    @ApiOperation(value = "用户信息修改")
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public HttpRes<List<User>> getAll() {
        return HttpRes.success(userService.getAllByFields(Lists.newArrayList("id", "name")));
    }
}
