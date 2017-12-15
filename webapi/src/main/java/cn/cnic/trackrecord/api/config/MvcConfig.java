package cn.cnic.trackrecord.api.config;

import cn.cnic.trackrecord.common.formatter.LongDateFormatter;
import cn.cnic.trackrecord.common.formatter.ShortDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addFormatter(new LongDateFormatter());
        registry.addFormatter(new ShortDateFormatter());
    }
}
