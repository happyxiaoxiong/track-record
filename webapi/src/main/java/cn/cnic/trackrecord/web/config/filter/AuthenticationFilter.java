package cn.cnic.trackrecord.web.config.filter;

import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.data.vo.AuthUser;
import cn.cnic.trackrecord.data.vo.Login;
import cn.cnic.trackrecord.web.identity.TokenUser;
import cn.cnic.trackrecord.web.identity.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* This filter maps to /session and tries to validate the username and password */
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private TokenUtils tokenUtil;
    private ObjectMapper objectMapper;

    public AuthenticationFilter(String urlMapping, String httpMethod, AuthenticationManager authenticationManager, TokenUtils tokenUtil, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(urlMapping, httpMethod, false));
        setAuthenticationManager(authenticationManager);
        this.tokenUtil = tokenUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Login loginVo = objectMapper.readValue(request.getInputStream(), Login.class);
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
        User user = ((TokenUser)authToken.getPrincipal()).getUser();
        String token = this.tokenUtil.createTokenForUser(user);
        user.setPassword(null);
        AuthUser userVo = new AuthUser(token, user);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), HttpRes.success(userVo));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), HttpRes.fail(failed.getMessage(), null));
    }

}
