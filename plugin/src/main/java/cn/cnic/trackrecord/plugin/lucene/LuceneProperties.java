package cn.cnic.trackrecord.plugin.lucene;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("plugin.lucene.conf")

public class LuceneProperties {
    private String indexPath;

}
