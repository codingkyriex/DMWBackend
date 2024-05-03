package com.example.dmwbackend.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/3 16:14
 */


public class TokenUtils {

    private static final String SECRET = "2939568716";

    public static String createToken(Integer userId) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        // 设置Token的过期时间 // 1小时后过期
        Date expireDate = new Date(System.currentTimeMillis() + 3600 * 1000);

        // 创建Token载荷
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        // 返回Token字符串
        return JWT.create()
                .withIssuer("auth0")
                .withExpiresAt(expireDate)
                .withClaim("userId", userId)
                .sign(algorithm);
    }

    public static boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); // 这里创建一个验证器，用于验证Token的签名

            // 使用验证器验证Token
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            // 如果Token签名验证失败，返回false
            return false;
        }
    }

    public static Long getUserIdFromToken(String token) {
        try {
            // 正确解码Token并获取用户ID
            DecodedJWT decodedJWT = JWT.decode(token);
            // 使用DecodedJWT对象来访问声明
            return decodedJWT.getClaim("userId").asLong();
        } catch (JWTDecodeException exception) {
            // 如果Token格式不正确，返回null
            return null;
        }
    }
}
