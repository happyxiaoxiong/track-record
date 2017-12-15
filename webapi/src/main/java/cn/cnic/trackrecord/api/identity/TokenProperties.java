package cn.cnic.trackrecord.api.identity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class TokenProperties {
    private String header = "Authorization";
    private String secret = "track_record";
    private long expiration = 20 * 60 * 1000;
    private String tokenHead = "tr_secret";
}
