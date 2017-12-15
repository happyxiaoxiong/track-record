package cn.cnic.trackrecord.common.date;

import cn.cnic.trackrecord.common.serializer.LongDateDeserializer;
import cn.cnic.trackrecord.common.serializer.LongDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
        LongDate intLongDate = new LongDate();
        try {
            intLongDate.setValue(dateToInt(dateFormat.parse(longDateStr)));
        } catch (ParseException e) {
            intLongDate.setValue(0);
        }
        return intLongDate;
    }
}
