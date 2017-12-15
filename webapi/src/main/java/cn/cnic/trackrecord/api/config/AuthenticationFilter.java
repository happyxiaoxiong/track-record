package cn.cnic.trackrecord.api.config;

import cn.cnic.trackrecord.api.identity.TokenUser;
import cn.cnic.trackrecord.api.identity.TokenUtil;
import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.model.entity.User;
import cn.cnic.trackrecord.vo.AuthUserVo;
import cn.cnic.trackrecord.vo.LoginVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* This filter maps to /session and tries to validate the username and password */
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private TokenUtil tokenUtil;
    private ObjectMapper objectMapper;

    protected AuthenticationFilter(String urlMapping, String httpMethod, AuthenticationManager authenticationManager, TokenUtil tokenUtil, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(urlMapping, httpMethod, false));
        setAuthenticationManager(authenticationManager);
        this.tokenUtil = tokenUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginVo loginVo = objectMapper.readValue(request.getInputStream(), LoginVo.class);
        //final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken("demo", "demo");
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginVo.getAccount(), loginVo.getPassword());
        try{
            return getAuthenticationManager().authenticate(authToken); // This will take to successfulAuthentication or faliureAuthentication function
        }
        catch(AuthenticationException e){
            throw new AuthenticationServiceException("account or password not correct");
        }
    }

    @Override
    protected void successfulAuthentication (HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authToken) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authToken);
        User user = ((TokenUser)authToken.getPrincipal()).getUser();
        user.setPassword("");
        String token = this.tokenUtil.createTokenForUser(user);

        AuthUserVo userVo = new AuthUserVo(token, user);
        objectMapper.writeValue(response.getWriter(), HttpRes.success(userVo));
    }


}
