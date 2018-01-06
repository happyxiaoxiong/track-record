package cn.cnic.trackrecord.data.vo;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.http.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "轨迹搜索字段")
@Setter
@Getter
public class TrackSearchParams extends Page {
    @ApiModelProperty(value = "轨迹名称", notes = "like匹配")
    private String name;
    @ApiModelProperty(value = "上传人", notes = "like匹配")
    private String userName;
    @ApiModelProperty(value = "开始时间")
    private LongDate startTime;
    @ApiModelProperty(value = "结束时间")
    private LongDate endTime;
    @ApiModelProperty(value = "最小经度")
    private Double minLongitude;
    @ApiModelProperty(value = "最大经度")
    private Double maxLongitude;
    @ApiModelProperty(value = "最小纬度")
    private Double minLatitude;
    @ApiModelProperty(value = "最大纬度")
    private Double maxLatitude;
}
