package cn.cnic.trackrecord.data.entity;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.date.ShortDate;
import cn.cnic.trackrecord.common.enumeration.Gender;
import cn.cnic.trackrecord.common.enumeration.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @ApiModelProperty(value = "主键", notes = "数据库自动生成")
    private int id;
    @ApiModelProperty(value = "账号", required = true, notes = "唯一")
    private String account;
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    @ApiModelProperty(value = "性别")
    private Gender gender;
    @ApiModelProperty(value = "生日", dataType = "string", notes = "格式:yyyy-MM-dd", example = "2017-11-11", reference = "string")
    private ShortDate birthday;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "工作单位")
    private String organization;
    @ApiModelProperty(value = "国家")
    private String country;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "县")
    private String county;
    @ApiModelProperty(value = "乡")
    private String township;
    @ApiModelProperty(value = "注册时间", dataType = "string", notes = "格式:yy-MM-dd HH:mm:ss", example = "2017-11-11 11:11:11", reference = "string")
    private LongDate addTime;
    @ApiModelProperty(value = "生日", dataType = "string", notes = "格式:yy-MM-dd HH:mm:ss", example = "2017-11-11 11:11:11", reference = "string")
    private LongDate loginTime;
    @ApiModelProperty(value = "性别")
    private Role role;
    @ApiModelProperty(value = "访问次数")
    private int visitCount;

}
