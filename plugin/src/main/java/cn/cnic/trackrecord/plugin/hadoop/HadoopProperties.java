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
    /**
     * 轨迹文件在hdfs的根目录
     */
    private String storePath = "default";
    /**
     * hadoop用户名，hadoop所在的用户名
     */
    private String user = "hadoop";
    /**
     * 当文件大于 (ratio * blockSize)时,不追加文件，单独写入
     */
    private double ratio = 0.75;
    /**
     * 块大小
     */
    private int blockSize = 1024 * 1024 * 128;
    /**
     * hadoop的安装目录
     */
    private String homeDir;
    /**
     * 给hadoop使用的本地临时目录，需要有权限
     */
    private String localTmpDir;
    /**
     * 原始文件在hdfs上存放的文件名
     */
    private String originFileName = "ori";
    /**
     * 转码后在hdfs上存放的文件名
     */
    private String thumbFileName = "thu";

    @PostConstruct
    public void init() {
        Files.createDirectory(localTmpDir);
    }
}
