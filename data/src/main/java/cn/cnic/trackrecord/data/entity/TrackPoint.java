package cn.cnic.trackrecord.data.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "轨迹点坐标")
@Data
public class TrackPoint {
    @ApiModelProperty("主键")
    private int id;
    @ApiModelProperty("轨迹id")
    private int trackId;
    @ApiModelProperty("经度")
    private double longitude;
    @ApiModelProperty("纬度")
    private double latitude;
    @ApiModelProperty("高度")
    private double altitude;
}
