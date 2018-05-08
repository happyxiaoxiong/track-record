package cn.cnic.trackrecord.web.api.controller;

import cn.cnic.trackrecord.common.enumeration.Gender;
import cn.cnic.trackrecord.common.enumeration.Role;
import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.common.http.HttpResCode;
import cn.cnic.trackrecord.web.Const;
import cn.cnic.trackrecord.web.config.property.TokenProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(value = "配置API", description = "配置API", tags = "Config")
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

    private Map<String, Object> tokenMap() {
        Map<String, Object> mp = new HashMap<>();
        mp.put("header", tokenProperties.getHeader());
        mp.put("head", tokenProperties.getTokenHead());
        mp.put("queryParam", tokenProperties.getQueryParam());
        mp.put("expiration", tokenProperties.getExpiration());
        return mp;
    }
}
