package com.hsy.springboot.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    /**
     * 生成token  header.payload.singature
     */
    private static final String SING = "XIAOSHUANG";

    public static String generateToken(Map<String, Object> map) {
        Calendar instance = Calendar.getInstance();
        // 默认7天过期
        instance.add(Calendar.DATE, 7);

        // 创建 JWT builder
        JWTCreator.Builder builder = JWT.create();

        // 遍历 map，添加 claims
        map.forEach((k, v) -> {
            if (v instanceof Integer) {
                builder.withClaim(k, (Integer) v);
            } else if (v instanceof Long) {
                builder.withClaim(k, (Long) v);
            } else if (v instanceof Boolean) {
                builder.withClaim(k, (Boolean) v);
            } else if (v instanceof Double) {
                builder.withClaim(k, (Double) v);
            } else if (v instanceof String) {
                builder.withClaim(k, (String) v);
            }
            // 其他类型的值可以在这里进行扩展
        });

        return builder.withExpiresAt(instance.getTime())  // 指定令牌过期时间
                .sign(Algorithm.HMAC256(SING));  // 签名
    }


    /**
     * 验证token  合法性
     */
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    public static Map<String, Object> parseToken(String token) {
        Map<String, Object> claims = new HashMap<>();
        try {
            DecodedJWT decodedJWT = verifyToken(token); // 先验证合法性

            decodedJWT.getClaims().forEach((key, value) -> {
                if (value.asString() != null) {
                    claims.put(key, value.asString());
                } else if (value.asInt() != null) {
                    claims.put(key, value.asInt());
                } else if (value.asLong() != null) {
                    claims.put(key, value.asLong());
                } else if (value.asBoolean() != null) {
                    claims.put(key, value.asBoolean());
                } else if (value.asDouble() != null) {
                    claims.put(key, value.asDouble());
                }
            });

        } catch (Exception e) {
            throw new RuntimeException("解析Token失败: " + e.getMessage(), e);
        }
        return claims;
    }
}



