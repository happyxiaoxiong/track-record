package cn.cnic.trackrecord.web.api.controller;

import cn.cnic.trackrecord.web.identity.TokenUser;
import cn.cnic.trackrecord.web.identity.TokenUtils;
import cn.cnic.trackrecord.web.Const;
import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.service.UserService;
import cn.cnic.trackrecord.data.vo.AuthUser;
import cn.cnic.trackrecord.data.vo.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(value = "用户授权api(登录,注册)", description = "用户授权api(登录,注册)", tags = "NoAuthUser")
@RestController
@RequestMapping(value = Const.API_ROOT + "no_auth")
public class NoAuthUserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "用户登录授权")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public HttpRes<AuthUser> login(@RequestBody Login login) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getAccount(), login.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User user = ((TokenUser) authentication.getPrincipal()).getUser();
        String token = tokenUtil.createTokenForUser(user);
        user.setPassword(null);
        AuthUser userVo = new AuthUser(token, user);
        return HttpRes.success(userVo);
    }

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public HttpRes<?> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.add(user);
        return HttpRes.success();
    }

    @ApiOperation(value = "判断账号是否存在")
    @RequestMapping(value = "exist", method = RequestMethod.POST)
    public HttpRes<Boolean> existAccount(@ApiParam(value = "账号", example = "test") @RequestParam String account) {
        return HttpRes.success(userService.existByAccount(account));
    }
}
