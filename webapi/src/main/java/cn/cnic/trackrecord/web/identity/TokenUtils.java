package cn.cnic.trackrecord.web.identity;

import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.web.config.property.TokenProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class TokenUtils {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenProperties tokenProperties;

    public Authentication verifyToken(HttpServletRequest request) {
        String token = request.getHeader(tokenProperties.getHeader());
        if (!StringUtils.hasLength(token)) {
            token = request.getParameter(tokenProperties.getQueryParam());
        }
        if (StringUtils.hasLength(token) && token.startsWith(tokenProperties.getTokenHead()) && token.length() > tokenProperties.getTokenHead().length()){
            final TokenUser user;
            try {
                user = parseUserFromToken(token.replace(tokenProperties.getTokenHead(),"").trim());
                return  new UserAuthentication(user);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    //Get User Info from the Token
    private TokenUser parseUserFromToken(String token) throws IOException {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
        User user = objectMapper.readValue((String)claims.get("user"), User.class);
        return new TokenUser(user);
    }

    public String createTokenForUser(User user) {
        try {
            return Jwts.builder()
                    .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getExpiration()))
                    .setSubject(user.getAccount())
                    .claim("user", objectMapper.writeValueAsString(user))
                    .signWith(SignatureAlgorithm.HS256, tokenProperties.getSecret())
                    .compact();
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
        }
        return "";
    }
}

