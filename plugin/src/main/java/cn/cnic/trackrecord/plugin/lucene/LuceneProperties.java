package cn.cnic.trackrecord.plugin.lucene;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@PropertySource(value = {"classpath:plugin-lucene.properties"})
@ConfigurationProperties("plugin.lucene.conf")
public class LuceneProperties {
    /**
     * 轨迹数据索引文件路径，需要有权限
     */
    private String indexPath = "";

}
