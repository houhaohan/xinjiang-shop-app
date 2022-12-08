package com.pinet.core.util;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
@Slf4j
public class JWTUtils {
    private static final String secret="a9fd38419340ee10";
    public static long expire = 7200;

    /**
     * 生成jwt token
     */
    public static String generateToken(String userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("token error : ", e);
            return null;
        }
    }

    public static String getUserId(String token) {
        try {
            Claims claims = getClaimByToken(token);
            if(claims == null){
                return null;
            }
            return claims.getSubject();
        } catch (Exception e) {
            log.debug("token error : ", e);
            return null;
        }
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public static boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }


    public static void main(String[] args) {

        JWTUtils utils = new JWTUtils();
        String token = utils.generateToken("10");
        System.out.println(token);

        String userId = getUserId(token);
        System.out.println("userId=====>"+userId);

//        Claims cli = utils.getClaimByToken(token);
//
//        Date expiration = cli.getExpiration();
//        System.out.println(JSONObject.toJSONString(cli));
//        System.out.println(expiration);

        //{"sub":"10","iat":1669342560,"exp":1669349760}
    }
}

