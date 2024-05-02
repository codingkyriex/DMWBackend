package com.example.dmwbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: 跨域处理
 * @author: eric
 * @time: 2024/5/2 11:26
 */
@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","DELETE")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
