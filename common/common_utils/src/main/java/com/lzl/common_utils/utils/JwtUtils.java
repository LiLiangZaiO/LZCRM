package com.lzl.common_utils.utils;

import com.lzl.common_utils.domain.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import java.util.Base64;
import java.util.UUID;

/**
 * 生成token以及校验token相关方法
 *
 * @author CaoChenLei
 */
public class JwtUtils {
    private static final String JWT_PAYLOAD_USER_KEY = "user";
    private static final String tokenSignKey = "LiZeLin";

    private static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    /**
     *加密token
     * @param userInfo
     * @param expire 过期时间，单位分钟
     * @return
     */
    public static String generateTokenExpireInMinutes(Object userInfo,int expire) {
        return Jwts.builder()
                //设置token主体部分，存储用户信息，
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toString(userInfo))
                .setId(createJTI())
                .setExpiration(DateTime.now().plusMinutes(expire).toDate())
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compact();
    }


    /**
     * 解析token
     */
    private static Jws<Claims> parserToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenSignKey)
                .parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, Class<T> userType) {
        Jws<Claims> claimsJws = parserToken(token);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setUserInfo(JsonUtils.toBean(body.get(JWT_PAYLOAD_USER_KEY).toString(), userType));
        claims.setExpiration(body.getExpiration());
        return claims;
    }

    /**
     * 获取token中的载荷信息
     *
     * @param token     用户请求中的令牌
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token) {
        Jws<Claims> claimsJws = parserToken(token);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setExpiration(body.getExpiration());
        return claims;
    }
}