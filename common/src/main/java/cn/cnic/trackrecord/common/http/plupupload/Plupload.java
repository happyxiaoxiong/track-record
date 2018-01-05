package cn.cnic.trackrecord.common.http.plupupload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@ApiModel("plupload上传参数")
@Data
public class Plupload {
    @ApiModelProperty("文件名")
    private String name;
    @ApiModelProperty(value = "文件分块总个数")
    private int chunks = -1;
    @ApiModelProperty(value = "当前块数,从0计数")
    private int chunk;
    @ApiModelProperty(value = "请求", hidden = true, notes = "不会自动赋值，需要手动传入")
    private HttpServletRequest request;
    @ApiModelProperty(value = "文件",notes = "不会自动赋值，需要手动传入")
    private MultipartFile file;

}
