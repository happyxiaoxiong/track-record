package cn.cnic.trackrecord.web.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
public class TokenProperties {
    private String header = "Authorization";
    private String secret = "tr_secret";
    private long expiration = 2 * 60 * 60 * 1000;
    private String tokenHead = "tr";
}
