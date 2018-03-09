package cn.cnic.trackrecord.data.entity;

import cn.cnic.trackrecord.common.date.ShortDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "轨迹统计信息")
@Data
public class TrackStat {
    @ApiModelProperty(value = "主键", notes = "数据库自动生成")
    private int id;
    @ApiModelProperty(value = "用户id")
    private int userId;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "巡护总时间(单位秒)")
    private int totalTime;
    @ApiModelProperty(value = "巡护总长度(单位米)")
    private double totalLength;
    @ApiModelProperty(value = "每月巡护天数")
    private int totalDay;
    @ApiModelProperty(value = "巡护总次数")
    private int totalCount;
    @ApiModelProperty(value = "0.按天统计,1.按月统计")
    private int type;
    @ApiModelProperty(value = "统计日期", dataType = "String", notes = "格式:yyyy-MM-dd", example = "2017-11-11", reference = "String")
    private ShortDate date;
}
