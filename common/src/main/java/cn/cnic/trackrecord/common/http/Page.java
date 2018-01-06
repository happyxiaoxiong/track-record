package cn.cnic.trackrecord.common.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "分页信息")
@Setter
@Getter
public class Page {
    @ApiModelProperty(value = "第几页", notes = "默认0")
    private int page = 0;
    @ApiModelProperty(value = "每页数量", notes = "默认5")
    private int size = 5;
}