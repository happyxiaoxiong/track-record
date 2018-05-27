package cn.cnic.trackrecord.data.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户密码修改")
@Data
public class UserChangePassword {
    @ApiModelProperty(value = "用户旧密码")
    private String oldPassword;
    @ApiModelProperty(value = "用户新密码")
    private String newPassword;
}
