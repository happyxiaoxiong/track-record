package cn.cnic.trackrecord.api.controller;

import cn.cnic.trackrecord.api.identity.TokenUser;
import cn.cnic.trackrecord.api.identity.TokenUtil;
import cn.cnic.trackrecord.api.Const;
import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.model.entity.User;
import cn.cnic.trackrecord.service.UserService;
import cn.cnic.trackrecord.vo.AuthUserVo;
import cn.cnic.trackrecord.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(value = "用户api", description = "用户api", tags = "User")
@RestController
@RequestMapping(value = Const.API_ROOT + "user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "用户登录授权")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public HttpRes<AuthUserVo> login(@RequestBody LoginVo login) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getAccount(), login.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User user = ((TokenUser) authentication.getPrincipal()).getUser();
        user.setPassword("");
        String token = tokenUtil.createTokenForUser(user);
        AuthUserVo userVo = new AuthUserVo(token, user);
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
    @ApiParam(name = "account", value = "账号", required = true, example = "test")
    @RequestMapping(value = "exist", method = RequestMethod.POST)
    public HttpRes<Boolean> existAccount(@RequestParam String account) {
        return HttpRes.success(userService.existByAccount(account));
    }
}
