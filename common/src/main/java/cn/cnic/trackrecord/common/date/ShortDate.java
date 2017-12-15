package cn.cnic.trackrecord.common.date;

import cn.cnic.trackrecord.common.serializer.ShortDateSerializer;
import cn.cnic.trackrecord.common.serializer.ShowDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@JsonDeserialize(using = ShowDateDeserializer.class)
@JsonSerialize(using = ShortDateSerializer.class)
public class ShortDate extends IntDate {
    public static ShortDate NullValue = new ShortDate(0);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ShortDate() { }

    private ShortDate(int value) {
        this.setValue(0);
    }

    @Override
    SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static ShortDate from(String shortDateStr) {
        ShortDate intShortDate = new ShortDate();
        try {
            intShortDate.setValue(dateToInt(dateFormat.parse(shortDateStr)));
        } catch (ParseException e) {
            intShortDate.setValue(0);
        }
        return intShortDate;
    }
}
