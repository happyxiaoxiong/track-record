package cn.cnic.trackrecord.common.formatter;

import cn.cnic.trackrecord.common.date.ShortDate;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class ShortDateFormatter implements Formatter<ShortDate> {
    @Override
    public ShortDate parse(String text, Locale locale) throws ParseException {
        return ShortDate.from(text);
    }

    @Override
    public String print(ShortDate date, Locale locale) {
        return date.getString();
    }
}
