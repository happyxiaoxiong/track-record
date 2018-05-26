package cn.cnic.trackrecord.common.date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用整数来表示日期
 */
@Data
public abstract class IntDate {
    /**
     * 用整数表示日期，精确到s
     */
    @ApiModelProperty(hidden = true)
    private int value = toInt(new Date().getTime());

    abstract SimpleDateFormat getDateFormat();

    /**
     * 整数日期转换成字符串
     * @return
     */
    @ApiModelProperty(hidden = true)
    public String getString() {
        if (getValue() == 0) {
            return "";
        }
        return getDateFormat().format(new Date(this.getTimeMillis()));
    }

    @ApiModelProperty(hidden = true)
    public long getTimeMillis() {
        return getValue() * 1000L;
    }

    /**
     * 转换成Java日期类
     * @return
     */
    public Date toDate() {
        return new Date(getTimeMillis());
    }

    /**
     * 毫秒转成秒
     * @param timeMillis
     * @return
     */
    static int toInt(long timeMillis) {
        return (int)(timeMillis / 1000);
    }
}
