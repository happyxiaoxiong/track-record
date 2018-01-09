package cn.cnic.trackrecord.plugin.lucene;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("plugin.lucene.conf")
public class LuceneProperties {
    private String indexPath = "";

}
