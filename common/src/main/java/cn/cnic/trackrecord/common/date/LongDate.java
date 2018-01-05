package cn.cnic.trackrecord.common.date;

import cn.cnic.trackrecord.common.serializer.LongDateDeserializer;
import cn.cnic.trackrecord.common.serializer.LongDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonDeserialize(using = LongDateDeserializer.class)
@JsonSerialize(using = LongDateSerializer.class)
public class LongDate extends IntDate {
    public static LongDate NullValue = new LongDate(0);

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public LongDate() {}

    private LongDate(int value) {
        this.setValue(value);
    }

    @Override
    SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static LongDate from(String longDateStr) {
        LongDate intLongDate = new LongDate(0);
        try {
            intLongDate.setValue(toInt(dateFormat.parse(longDateStr).getTime()));
        } catch (ParseException ignored) {
        }
        return intLongDate;
    }

    public static LongDate from(long timeMillis) {
        return new LongDate(toInt(timeMillis));
    }

    public static LongDate from(Date date) {
        return new LongDate(toInt(date.getTime()));
    }
}
