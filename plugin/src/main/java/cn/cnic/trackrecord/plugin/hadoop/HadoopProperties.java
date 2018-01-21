package cn.cnic.trackrecord.plugin.hadoop;

import cn.cnic.trackrecord.common.util.Files;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@PropertySource(value = {"classpath:plugin-hadoop.properties"})
@ConfigurationProperties(prefix = "plugin.hadoop.conf")
@Setter
@Getter
public class HadoopProperties {
    private String uri = "hdfs://localhost:9000/";
    private String storePath = "default";
    private String user = "hadoop";
    private int blockSize = 1024 * 1024 * 128;
    private String homeDir;
    private String localTmpDir;

    @PostConstruct
    public void init() {
        Files.createDirectory(localTmpDir);
    }
}
