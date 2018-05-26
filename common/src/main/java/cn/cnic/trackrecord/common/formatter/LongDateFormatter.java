package cn.cnic.trackrecord.common.formatter;

import cn.cnic.trackrecord.common.date.LongDate;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * LongDate转换器
 */
public class LongDateFormatter implements Formatter<LongDate> {
    @Override
    public LongDate parse(String text, Locale locale) throws ParseException {
        return LongDate.from(text);
    }

    @Override
    public String print(LongDate date, Locale locale) {
        return date.getString();
    }
}
