package cn.cnic.trackrecord.data.kml;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@ApiModel(value = "kml轨迹信息")
@Data
public class RouteRecord {
    @ApiModelProperty(value = "标注列表")
    private List<PlaceMark> placeMarks = new LinkedList<>();
}
