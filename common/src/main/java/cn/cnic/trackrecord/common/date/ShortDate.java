package cn.cnic.trackrecord.common.date;

import cn.cnic.trackrecord.common.serializer.ShortDateSerializer;
import cn.cnic.trackrecord.common.serializer.ShowDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonDeserialize(using = ShowDateDeserializer.class)
@JsonSerialize(using = ShortDateSerializer.class)
public class ShortDate extends IntDate {
    public final static ShortDate NullValue = new ShortDate(0);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ShortDate() { }

    public ShortDate(int value) {
        this.setValue(value);
    }

    @Override
    SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static ShortDate from(String shortDateStr) {
        try {
            return new ShortDate(toInt(dateFormat.parse(shortDateStr).getTime()));
        } catch (ParseException ignored) {
        }
        return NullValue;
    }

    public static ShortDate from(long timeMillis) {
        return new ShortDate(toInt(timeMillis));
    }

    public static ShortDate from(Date date) {
        return new ShortDate(toInt(date.getTime()));
    }
}
