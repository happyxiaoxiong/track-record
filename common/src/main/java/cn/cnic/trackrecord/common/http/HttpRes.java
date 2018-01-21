package cn.cnic.trackrecord.common.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "响应内容")
@Data
public class HttpRes<T> {
    @ApiModelProperty(value = "编码", required = true)
    private int code;

    @ApiModelProperty(value = "信息说明")
    private String message;

    @ApiModelProperty(value = "数据")
    private T data;

    public HttpRes() {
    }

    public HttpRes(HttpResCode code, String message, T data) {
        this.code = code.getCode();
        this.message = message;
        this.data = data;
    }

    public static <T> HttpRes<T> success() {
        return create(HttpResCode.SUCCESS, "success", null);
    }
    public static <T> HttpRes<T> success(T data) {
        return create(HttpResCode.SUCCESS, "success", data);
    }

    public static <T> HttpRes<T> success(String message, T data) {
        return create(HttpResCode.SUCCESS, message, data);
    }

    public static <T> HttpRes<T> fail() {
        return create(HttpResCode.FAIL, "fail", null);
    }

    public static <T> HttpRes<T> fail(T data) {
        return create(HttpResCode.FAIL, "fail", data);
    }

    public static <T> HttpRes<T> fail(String message, T data) {
        return create(HttpResCode.FAIL, message, data);
    }

    public static <T> HttpRes<T> create(HttpResCode code, String message, T data) {
        return new HttpRes<>(code, message, data);
    }
}
