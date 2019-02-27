package cn.notemi.core.security;

import cn.notemi.controller.APIController;
import cn.notemi.core.security.provider.PasswordAuthenticationProvider;
import cn.notemi.core.security.provider.TokenAuthenticationProvider;
import cn.notemi.core.security.provider.WeChatAppAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * Title：ServiceSecurityConfig
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
@Configuration
@EnableScheduling
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ServiceSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .anonymous().disable()
            .exceptionHandling()
            .authenticationEntryPoint(unAuthorizedEntryPoint());
        // 添加过滤器
        http.addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            //.anyRequest();
            .antMatchers(HttpMethod.POST, APIController.getNoAuthPostUrls())
            .and()
            .ignoring()
            .antMatchers(HttpMethod.GET, APIController.getNoAuthGetUrls());
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(passwordAuthenticationProvider())
            .authenticationProvider(tokenAuthenticationProvider())
            .authenticationProvider(weChatAppAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider passwordAuthenticationProvider() {
        return new PasswordAuthenticationProvider();
    }

    @Bean
    public AuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider();
    }

    @Bean
    public AuthenticationProvider weChatAppAuthenticationProvider() {
        return new WeChatAppAuthenticationProvider();
    }

    public AuthenticationEntryPoint unAuthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
