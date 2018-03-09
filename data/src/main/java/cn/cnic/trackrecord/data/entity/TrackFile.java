package cn.cnic.trackrecord.data.entity;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.enumeration.TrackFileState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "轨迹文件上传信息")
@Data
public class TrackFile {
    @ApiModelProperty(value = "主键", notes = "数据库自动生成")
    private int id;
    @ApiModelProperty(value = "用户id")
    private int userId;
    @ApiModelProperty(value = "轨迹文件状态", dataType = "int")
    private TrackFileState state;
    @ApiModelProperty(value = "轨迹上传时间", dataType = "String", notes = "格式:yy-MM-dd HH:mm:ss", example = "2017-11-11 11:11:11", reference = "String")
    private LongDate uploadTime;
    @ApiModelProperty(value = "轨迹路径信息")
    private String path;
    @ApiModelProperty(value = "轨迹文件名称")
    private String filename;
    @ApiModelProperty(value = "轨迹文件大小")
    private int fileSize;
    @ApiModelProperty(value = "文件md5")
    private String md5;
    @ApiModelProperty(value = "说明")
    private String comment;
    @ApiModelProperty(value = "重试次数")
    private int tries;
    @ApiModelProperty(value = "更新时间", dataType = "String", notes = "格式:yy-MM-dd HH:mm:ss", example = "2017-11-11 11:11:11", reference = "String")
    private LongDate updateTime;
}
