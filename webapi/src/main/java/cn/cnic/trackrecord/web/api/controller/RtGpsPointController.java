package cn.cnic.trackrecord.web.api.controller;

import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.data.entity.RtGpsPoint;
import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.service.RtGpsPointService;
import cn.cnic.trackrecord.web.Const;
import cn.cnic.trackrecord.web.identity.UserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "用户实时位置api", description = "用户实时位置api", tags = "RtGpsPoint")
@RestController
@RequestMapping(value = Const.API_ROOT + "rt")
public class RtGpsPointController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RtGpsPointService rtGpsPointService;

    @ApiOperation(value = "获取所有用户的当前在线位置")
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public HttpRes<List<RtGpsPoint>> getAll() {
        return HttpRes.success(rtGpsPointService.getAll());
    }

    @ApiOperation(value = "跟新位置信息", notes = "id和userId和userName三个参数不用设置,time参数格式为yyyy-MM-dd HH:mm:ss")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public HttpRes<Boolean> updatePosition( @RequestBody RtGpsPoint gpsPoint) {
        User user = userDetailsService.getLoginUser();
        gpsPoint.setUserId(user.getId());
        gpsPoint.setUserName(user.getName());
        return HttpRes.success(rtGpsPointService.add(gpsPoint) > 0);
    }
}
