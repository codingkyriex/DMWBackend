package com.example.dmwbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/3 16:22
 */

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 创建TokenInterceptor的实例
        TokenInterceptor tokenInterceptor = new TokenInterceptor();

        // 添加拦截器，并指定拦截路径模式
        registry.addInterceptor(tokenInterceptor)
                // 排除不需要拦截的路径
                .excludePathPatterns("/user/**") // 排除 /login 和 /login开头的所有路径
                .excludePathPatterns("/article/detail/**")
                .excludePathPatterns("/article/list")
                .excludePathPatterns("/article/pictures")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/home/**")
                // 可以添加更多的排除路径
                // .excludePathPatterns("/public/**", "/css/**", "/js/**", "/images/**")

                // 添加需要拦截的路径模式
                .addPathPatterns("/**"); // 拦截所有请求
    }
}
