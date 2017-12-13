package cn.cnic.trackrecord.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int uid;
    private String account;
    private String password;
    private String name;
    private int gender;
    private Timestamp birthday;
    private String email;
    private String organization;
    private String country;
    private String province;
    private String city;
    private String county;
    private String township;
    private Timestamp addTime;
    private Timestamp loginTime;
    private int role;
    private int visitCount;
}
