package cn.cnic.trackrecord.data.entity;

import cn.cnic.trackrecord.common.date.LongDate;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RtGpsPoint {
    @ApiModelProperty("主键")
    private int id;
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("经度")
    private double longitude;
    @ApiModelProperty("纬度")
    private double latitude;
    @ApiModelProperty("高度")
    private double altitude;
    @ApiModelProperty(value = "时间", dataType = "string", notes = "格式: yyyy-MM-dd HH:mm:ss")
    private LongDate time;
}
