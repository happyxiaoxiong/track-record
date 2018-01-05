package cn.cnic.trackrecord.web.config;

import cn.cnic.trackrecord.common.formatter.LongDateFormatter;
import cn.cnic.trackrecord.common.formatter.ShortDateFormatter;
import cn.cnic.trackrecord.common.hadoop.HadoopBean;
import cn.cnic.trackrecord.common.http.plupupload.PluploadBean;
import cn.cnic.trackrecord.common.kmz.UnzipBean;
import cn.cnic.trackrecord.web.config.property.HadoopConfProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private HadoopConfProperties hadoopConfProperties;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addFormatter(new LongDateFormatter());
        registry.addFormatter(new ShortDateFormatter());
    }

    @Bean
    public PluploadBean pluploadBean() {
        return new PluploadBean();
    }

    @Bean
    public UnzipBean unzipBean() {
        return new UnzipBean();
    }

    @Bean
    public HadoopBean hadoopBean() {
        return new HadoopBean(hadoopConfProperties.getHadoopConf());
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
}
