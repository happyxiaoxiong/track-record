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
    @ApiModelProperty(value = "关键字(轨迹名称、记录人、关键地点、说明)")
    private String keyword;
    @ApiModelProperty(value = "开始巡护时间")
    private LongDate startTime;
    @ApiModelProperty(value = "结束巡护时间")
    private LongDate endTime;
    @ApiModelProperty(value = "中心点经度")
    private Double longitude;
    @ApiModelProperty(value = "中心点纬度")
    private Double latitude;
    @ApiModelProperty(value = "离中心点距离")
    private double distance;
}
