package cn.cnic.trackrecord.plugin.ffmpeg;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@PropertySource(value = {"classpath:plugin-ffmpeg.properties"})
@ConfigurationProperties(prefix = "plugin.ffmpeg.conf")
@Setter
@Getter
public class FfmpegProperties {
    /**
     * ffmpeg的bin路径
     */
    private String path;

}
