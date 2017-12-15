package cn.cnic.trackrecord.api.identity;

import cn.cnic.trackrecord.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Component
public class TokenUtil {

    @Autowired
    private TokenProperties tokenProperties;

    public Authentication verifyToken(HttpServletRequest request) {
        final String token = request.getHeader(tokenProperties.getHeader());

        if (StringUtils.hasLength(token) && token.startsWith(tokenProperties.getTokenHead())){
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

        User user = (User) claims.get("user");
        return new TokenUser(user);
    }

    public String createTokenForUser(TokenUser tokenUser) {
        return createTokenForUser(tokenUser.getUser());
    }

    public String createTokenForUser(User user) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getExpiration()))
                .setSubject(user.getAccount())
                .claim("user", user)
                .signWith(SignatureAlgorithm.HS256, tokenProperties.getSecret())
                .compact();
    }
}

