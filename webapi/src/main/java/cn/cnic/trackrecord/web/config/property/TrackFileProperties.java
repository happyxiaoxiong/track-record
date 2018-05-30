package cn.cnic.trackrecord.web.config.property;

import cn.cnic.trackrecord.common.util.Files;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "track.file")
@Setter
@Getter
public class TrackFileProperties {
    /**
     * 解压文件尝试次数
     */
    private int tries = 3;
    /**
     * 轨迹文件上传路径，需要有权限
     */
    private String uploadPath;
    /**
     * 轨迹文件解压路径，需要有权限
     */
    private String unzipPath;
    /**
     * 轨迹文件处理任务间隔时间，该字段已废弃
     */
    @Deprecated
    private long fixedDelay = 5000;
    /**
     * TrackDetail的文件名
     */
    private String trackDetailFileName = "TrackDetail.xml";
    /**
     * RouteRecord的文件名
     */
    private String routeRecordFileName = "RouteRecord.kml";

    @PostConstruct
    public void init() {
        // 创建目录
        Files.createDirectory(uploadPath);
        Files.createDirectory(unzipPath);
    }
}
