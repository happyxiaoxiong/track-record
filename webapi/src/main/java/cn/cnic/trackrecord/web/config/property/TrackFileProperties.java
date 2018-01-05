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
    private int tries = 3;
    private String uploadPath;
    private String unzipPath;
    private long fixedDelay = 5000;
    private String trackDetailFileName = "TrackDetail.xml";
    private String routeRecordFileName = "RouteRecord.kml";

    @PostConstruct
    public void init() {
        Files.createDirectory(uploadPath);
        Files.createDirectory(unzipPath);
    }
}
