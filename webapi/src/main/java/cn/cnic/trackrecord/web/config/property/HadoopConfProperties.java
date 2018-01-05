package cn.cnic.trackrecord.web.config.property;

import cn.cnic.trackrecord.common.hadoop.Conf;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hadoop.conf")
@Setter
@Getter
public class HadoopConfProperties {
    private String uri = "hdfs://localhost:9000/";
    private String trackPath = "";
    private String user = "hadoop";
    private int blockSize = 1024 * 1024 * 64;
    private String home;

    public Conf getHadoopConf() {
        Conf conf = new Conf();
        conf.setUri(uri);
        conf.setFilePath(trackPath);
        conf.setUser(user);
        conf.setBlockSize(blockSize);
        conf.setHome(home);
        return conf;
    }
}
