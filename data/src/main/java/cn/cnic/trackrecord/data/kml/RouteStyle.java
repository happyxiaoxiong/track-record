package cn.cnic.trackrecord.data.kml;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "轨迹路线样式")
@Data
public class RouteStyle {
    @ApiModelProperty(value = "颜色")
    private String color;
    @ApiModelProperty(value = "宽度")
    private String width;
    @ApiModelProperty(value = "id")
    private String id;
}
