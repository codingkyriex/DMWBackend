package com.example.dmwbackend.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 12:59
 */
public class TokenGenerator {

    public static String generateToken(String apiKey, int expSeconds) {
        try {
            String[] parts = apiKey.split("\\.");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid apiKey format");
            }
            String id = parts[0];
            String secret = parts[1];

            long expMillis = System.currentTimeMillis() + (expSeconds * 1000L);
            long timestamp = System.currentTimeMillis();

            Map<String, Object> payload = new HashMap<>();
            payload.put("api_key", id);
            payload.put("exp", expMillis);
            payload.put("timestamp", timestamp);

            String token = Jwts.builder()
                    .setClaims(payload)
                    .setHeaderParam("alg", "HS256")
                    .setHeaderParam("sign_type", "SIGN")
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();

            return token;
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }
}
