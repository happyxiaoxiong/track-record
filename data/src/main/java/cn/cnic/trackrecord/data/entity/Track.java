package cn.cnic.trackrecord.data.entity;

import cn.cnic.trackrecord.common.date.LongDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "轨迹信息")
@Data
public class Track {
    @ApiModelProperty(value = "主键", notes = "数据库自动生成")
    private int id;
    @ApiModelProperty(value = "轨迹名称")
    private String name;
    @ApiModelProperty(value = "用户id")
    private int userId;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "轨迹开始时间", dataType = "String", notes = "格式:yy-MM-dd HH:mm:ss", example = "2017-11-11 11:11:11")
    private LongDate startTime;
    @ApiModelProperty(value = "轨迹结束时间", dataType = "String", notes = "格式:yy-MM-dd HH:mm:ss", example = "2017-11-11 11:11:11")
    private LongDate endTime;
    @ApiModelProperty(value = "轨迹长度")
    private double length;
    @ApiModelProperty(value = "轨迹最高海拔")
    private double maxAltitude;
    @ApiModelProperty(value = "轨迹关键地点列表名称")
    private String keySitesList;
    @ApiModelProperty(value = "轨迹路径信息")
    private String path;
    @ApiModelProperty(value = "轨迹文件大小")
    private int fileSize;
    @ApiModelProperty(value = "轨迹文件名称")
    private String filename;
    @ApiModelProperty(value = "文件md5")
    private String md5;
    @ApiModelProperty(value = "上传时间", dataType = "String", notes = "格式:yy-MM-dd HH:mm:ss", example = "2017-11-11 11:11:11")
    private LongDate uploadTime;
    @ApiModelProperty(value = "注释说明")
    private String annotation;
}
