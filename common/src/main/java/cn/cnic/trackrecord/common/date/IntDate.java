package cn.cnic.trackrecord.common.date;

import lombok.*;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public abstract class IntDate {
    /**
     * 用整数表示日期，精确到s
     */
    private int value = toInt(new Date().getTime());

    abstract SimpleDateFormat getDateFormat();

    public String getString() {
        if (getValue() == 0) {
            return "";
        }
        return getDateFormat().format(new Date(this.getTimeMillis()));
    }

    public long getTimeMillis() {
        return getValue() * 1000L;
    }

    static int dateToInt(Date date) {
        return toInt(date.getTime());
    }

    static private int toInt(long timeMillis) {
        return (int)(timeMillis / 1000);
    }
}
