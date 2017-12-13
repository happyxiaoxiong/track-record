package cn.cnic.trackrecord.api.controller;

import cn.cnic.trackrecord.common.constant.web.WebConst;
import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.model.User;
import cn.cnic.trackrecord.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = WebConst.API_ROOT + "user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public HttpRes<User> login(@RequestParam String account, @RequestParam String password) {
        User user = userService.getByAccountAndPassword(account, password);
        return user == null ? HttpRes.fail() : HttpRes.success(user);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public HttpRes<?> register(@RequestBody User user) {
        return HttpRes.fail();
    }
}
