package cn.cnic.trackrecord.data.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("登录参数")
@Data
public class LoginVo {
    @ApiModelProperty(value = "账号", required = true)
    private String account;
    @ApiModelProperty(value = "密码", required = true)
    private String password;
}
