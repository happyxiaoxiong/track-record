package cn.cnic.trackrecord.api.controller;

import cn.cnic.trackrecord.api.identity.TokenProperties;
import cn.cnic.trackrecord.common.enumeration.Gender;
import cn.cnic.trackrecord.common.enumeration.Role;
import cn.cnic.trackrecord.common.http.HttpResCode;
import cn.cnic.trackrecord.api.Const;
import cn.cnic.trackrecord.common.http.HttpRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(value = "配置api", description = "配置api", tags = "Config")
@RestController
@RequestMapping(Const.API_ROOT + "config")
public class ConfigController {

    @Autowired
    private TokenProperties tokenProperties;

    @ApiOperation(value = "返回server相关配置")
    @RequestMapping(method = RequestMethod.GET)
    public HttpRes<Map<String, Object>> httpResCodeMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("http_res_code", HttpResCode.codeMap());
        result.put("token", tokenMap());
        result.put("gender", Gender.values());
        result.put("role", Role.values());
        return HttpRes.success(result);
    }

    private Map<String, String> tokenMap() {
        Map<String, String> mp = new HashMap<>();
        mp.put("header", tokenProperties.getHeader());
        mp.put("head", tokenProperties.getTokenHead());
        return mp;
    }
}
