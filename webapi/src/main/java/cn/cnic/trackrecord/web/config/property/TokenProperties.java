package cn.cnic.trackrecord.web.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 前端可以使用两种方式来传jwt token，一种的通过设置头部参数，另一种通过传参
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
public class TokenProperties {
    /**
     * token的头部字段名
     */
    private String header = "Authorization";
    /**
     * token的查询参数名
     */
    private String queryParam = "token";
    /**
     * jwt token的密钥
     */
    private String secret = "tr_secret";
    /**
     * 过期时间，单位s
     */
    private long expiration = 2 * 60 * 60 * 1000;

    /**
     * jwt token 前缀
     */
    private String tokenHead = "tr";
}
