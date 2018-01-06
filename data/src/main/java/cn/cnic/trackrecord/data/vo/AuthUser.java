package cn.cnic.trackrecord.data.vo;

import cn.cnic.trackrecord.data.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("登录的用户信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("用户信息")
    private User user;
}
