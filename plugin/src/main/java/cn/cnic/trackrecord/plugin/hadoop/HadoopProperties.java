package cn.cnic.trackrecord.plugin.hadoop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:plugin-hadoop.properties"})
@ConfigurationProperties(prefix = "plugin.hadoop.conf")
@Setter
@Getter
public class HadoopProperties {
    private String uri = "hdfs://localhost:9000/";
    private String filePath = "default";
    private String user = "hadoop";
    private int blockSize = 1024 * 1024 * 64;
    private String homeDir;

}
