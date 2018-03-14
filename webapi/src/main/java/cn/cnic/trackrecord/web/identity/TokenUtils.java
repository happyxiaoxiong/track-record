package cn.cnic.trackrecord.web.identity;

import cn.cnic.trackrecord.common.util.Objects;
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
            final TokenUser user = parseUserFromToken(token.replace(tokenProperties.getTokenHead(),"").trim());
            if (Objects.nonNull(user)) {
                return  new UserAuthentication(user);
            }
        }
        return null;
    }

    //Get User Info from the Token
    public TokenUser parseUserFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(tokenProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();

        User user = null;
        try {
            user = objectMapper.readValue((String)claims.get("user"), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TokenUser(user);
    }

    public String createTokenForUser(TokenUser tokenUser) {
        return createTokenForUser(tokenUser.getUser());
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

