package cn.cnic.trackrecord.data.kml;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "标注信息")
@Data
public abstract class PlaceMark {
    @ApiModelProperty(value = "名称")
    private String name;
}
