package cn.cnic.trackrecord.data.vo;

import lombok.Data;

@Data
public class UserChangePassword {
    private String oldPassword;
    private String newPassword;
}
