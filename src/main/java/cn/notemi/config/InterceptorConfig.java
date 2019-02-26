package cn.notemi.config;

import cn.notemi.core.intercepter.ResponseResultInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Title：InterceptorConfig
 * Description：
 *
 * @author Flicker
 * @create 2019/2/26 2:23 PM
 **/
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    private ResponseResultInterceptor responseResultInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //响应结果控制拦截
        registry
                .addInterceptor(responseResultInterceptor)
                .addPathPatterns("/**");
    }

}
