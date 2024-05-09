package com.example.dmwbackend.config;


import com.example.dmwbackend.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/3 16:21
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        // 尝试从请求头中获取Authorization
        String token = request.getHeader("Authorization");
        System.out.println(token);
        boolean tokenValid = false;
        tokenValid = TokenUtils.verifyToken(token);
        System.out.println(TokenUtils.getUserIdFromToken(token));

        if(token==null){
            System.out.println("error: no token");
            request.setAttribute("tokenError", "Token is missing.");
            return false;
        }
        // 如果Token无效或缺失
        if (!tokenValid) {
            System.out.println("error: no valid token");
            request.setAttribute("tokenError", "no valid token.");
            return false;
        }

        // 如果Token有效，继续处理请求
        return true;
    }
}
