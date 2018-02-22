package cn.cnic.trackrecord.web.config;

import cn.cnic.trackrecord.common.formatter.LongDateFormatter;
import cn.cnic.trackrecord.common.formatter.ShortDateFormatter;
import cn.cnic.trackrecord.common.http.plupupload.PluploadBean;
import cn.cnic.trackrecord.core.track.TrackLuceneFormatter;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addFormatter(new LongDateFormatter());
        registry.addFormatter(new ShortDateFormatter());
    }

    @Bean
    public TrackLuceneFormatter trackLuceneFormatter() {
        return new TrackLuceneFormatter();
    }

    @Bean
    public PluploadBean pluploadBean() {
        return new PluploadBean();
    }

    @Bean
    public Executor executor() {
        return Executors.newFixedThreadPool(1);
    }

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(executor());
        return eventMulticaster;
    }

    @Bean
    public RestTemplate restTemplate(HttpMessageConverters messageConverters) {
        return new RestTemplate(messageConverters.getConverters());
    }
}
