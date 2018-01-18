package cn.cnic.trackrecord.web.api.controller;

import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.web.Const;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Api(value = "其他API", description = "其他API", tags = "Extra")
@RestController
@RequestMapping(Const.API_ROOT + "extra")
public class ExtraController {
    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "获取qq_convertor js")
    @RequestMapping(value = "qq/convertor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpRes<String> qqConvertor() {
        // fix problem: front doesn't dynamic import qq.convert, because qq convert js contains
        return HttpRes.success(restTemplate.getForObject("http://map.qq.com/api/js?v=2.exp&libraries=convertor&key=IMZBZ-S7VRW-NXERI-RLSJY-HHHCT-MBFI4", String.class));
    }
}
