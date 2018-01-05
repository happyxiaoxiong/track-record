package cn.cnic.trackrecord.data.kml;

import cn.cnic.trackrecord.data.entity.TrackPoint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("关键点坐标信息标注")
public class KeyPointPlaceMark extends PlaceMark {
    @ApiModelProperty(value = "关键点描述")
    private List<String> desc;
    @ApiModelProperty(value = "关键点坐标")
    private TrackPoint point;
}
