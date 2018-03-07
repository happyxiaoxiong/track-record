package cn.cnic.trackrecord;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("cn.cnic.trackrecord.dao")
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class WebApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WebApiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(WebApiApplication.class);
	}
}
