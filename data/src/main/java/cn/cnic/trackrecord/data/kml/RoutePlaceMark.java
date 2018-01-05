package cn.cnic.trackrecord.data.kml;

import cn.cnic.trackrecord.data.entity.TrackPoint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("轨迹坐标点信息标注")
@Data
public class RoutePlaceMark extends PlaceMark {
    @ApiModelProperty(value = "轨迹点坐标")
    private List<TrackPoint> points;
    @ApiModelProperty(value = "轨迹的样式")
    private RouteStyle style;
}
