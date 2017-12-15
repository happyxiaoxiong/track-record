package cn.cnic.trackrecord.api.controller;

import cn.cnic.trackrecord.common.http.HttpResCode;
import cn.cnic.trackrecord.api.Const;
import cn.cnic.trackrecord.common.http.HttpRes;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(value = "配置api", description = "配置api", tags = "Config")
@RestController
@RequestMapping(Const.API_ROOT + "config")
public class ConfigController {

    @RequestMapping(value = "http_res_code", method = RequestMethod.GET)
    public HttpRes<Map<String, Integer>> httpResCodeMap() {
        return HttpRes.success(HttpResCode.httpResCodeMap());
    }
}
