package cn.cnic.trackrecord.web.config.filter;

import cn.cnic.trackrecord.common.util.Objects;
import cn.cnic.trackrecord.web.identity.TokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证过滤器，验证jwt token是否合法
 */
public class VerifyTokenFilter extends GenericFilterBean {

    private final TokenUtils tokenUtil;

    public VerifyTokenFilter(TokenUtils tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request  = (HttpServletRequest)  servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Authentication authentication = null;
        try {
            authentication = tokenUtil.verifyToken(request);
            if (Objects.nonNull(authentication)) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch (ExpiredJwtException e) {
            //440会话超时
            response.setStatus(440);
        }
        catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        finally {
            if (Objects.nonNull(authentication)) {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        }
    }
}
