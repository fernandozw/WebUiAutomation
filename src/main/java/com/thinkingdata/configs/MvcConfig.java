package com.thinkingdata.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/11/25 3:16 PM
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    // 允许访问的域名
    @Value(value = "${corsFilter.allowOrigins}")
    private String origins;
    // 允许的请求方式
    @Value(value = "${corsFilter.allowedMethods}")
    private String methods;

    /**
     * 解决跨域请求
     *
     * @return
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins(origins.split(","))
                .allowedHeaders("*")
                .allowedMethods(methods.split(","))
                .maxAge(3600);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}

