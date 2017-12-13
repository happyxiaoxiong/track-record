package cn.cnic.trackrecord.api.controller;

import cn.cnic.trackrecord.common.constant.web.HttpResCode;
import cn.cnic.trackrecord.common.constant.web.WebConst;
import cn.cnic.trackrecord.common.http.HttpRes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(WebConst.API_ROOT + "config")
public class ConfigController {

    @RequestMapping(value = "http_res_code", method = RequestMethod.GET)
    public HttpRes<Map<String, Integer>> httpResCodeMap() {
        return HttpRes.success(HttpResCode.httpResCodeMap());
    }
}
