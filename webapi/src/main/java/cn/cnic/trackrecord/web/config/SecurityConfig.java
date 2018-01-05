package cn.cnic.trackrecord.web.config;

import cn.cnic.trackrecord.web.Const;
import cn.cnic.trackrecord.web.config.filter.AuthenticationFilter;
import cn.cnic.trackrecord.web.config.filter.CorsFilter;
import cn.cnic.trackrecord.web.config.filter.VerifyTokenFilter;
import cn.cnic.trackrecord.web.identity.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenUtils tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(userDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许对于网站静态资源的无授权访问,对于获取token的rest api要允许匿名访问
                .antMatchers("/", "/v2/api-docs", "/swagger-ui/**", "/swagger-resources/**", "/*.html", "/**/*.html" ,
                        "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.gif", "/**/*.svg", "/**/*.ico", "/**/*.ttf", "/**/*.woff",
                        Const.API_ROOT + "no_auth/**", Const.API_ROOT + "config"
                ).permitAll()
                .and()
                // Add CORS Filter
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                // custom JSON based authentication by POST of {"account":"<account>","password":"<password>"} which sets the token header upon authentication
                .addFilterBefore(new AuthenticationFilter(Const.API_ROOT + "no_auth/login", HttpMethod.POST.name(), authenticationManager(), tokenUtil, objectMapper), UsernamePasswordAuthenticationFilter.class)
                // Custom Token based authentication based on the header previously given to the client
                .addFilterBefore(new VerifyTokenFilter(tokenUtil), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
