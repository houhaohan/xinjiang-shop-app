package com.pinet.core.util;

import com.pinet.core.entity.Token;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Slf4j
public class AppJwtTokenUtil {
    private static final Long EXPIRE_TIME = 7 * 24 * 60 * 60L;
    private static final Long GRACE_TIME = 30 * 24 * 60 * 60L;

    /**
     * 获取jwt失效时间
     */
    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }


    /**
     * <pre>
     *  验证token是否失效
     *  true:过期   false:没过期
     * </pre>
     */
    public static Boolean isTokenExpired(String token) {
        try{
            final Date expiration = getExpirationDateFromToken(token);
            System.out.println("效验token");
            return expiration.before(new Date());
        }catch (ExpiredJwtException e){
            return true;
        }catch (SignatureException e){
            log.error("jwt 签名错误", e);
            return true;
        }catch (Exception e){
            log.error("jwt token未知错误", e);
            return true;
        }

    }

    /**
     * 获取jwt的payload部分
     */
    public static Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey("defaultSecret")
                .parseClaimsJws(token)
                .getBody();
    }


    public static Token generateTokenObject(String userName, HttpServletRequest request){
        String token = generateToken(userName);

        Long expireTime = System.currentTimeMillis() + EXPIRE_TIME * 1000;

        Long graceTime = expireTime + GRACE_TIME * 1000;

        Token customerToken = new Token();
        customerToken.setToken(token);
        customerToken.setCustomerId(parseLong(userName, 0));
        customerToken.setCreateTime(System.currentTimeMillis());
        customerToken.setExpireTime(expireTime);
        customerToken.setGraceTime(graceTime);
        customerToken.setIsBlackmail(0);
        customerToken.setTerminal(getTerminal(request));
        return customerToken;
    }

    /**
     * 生成token(通过用户名和签名时候用的随机数)
     */
    public static String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        String randomKey = getRandomKey();
        claims.put("randomKey", randomKey);
        return doGenerateToken(claims, userName);
    }


    /**
     * 生成token
     */
    private static String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + 604800 * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, "defaultSecret")
                .compact();
    }


    /**
     * 获取混淆MD5签名用的随机字符串
     */
    public static String getRandomKey() {
        return getRandomString(6);
    }

    /**
     * 获取随机位数的字符串
     *
     * @author fengshuonan
     * @Date 2017/8/24 14:09
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 强转成长整形
     * */
    public static long parseLong(Object obj, long _default){
        if(obj == null){
            return _default;
        }
        try{
            return Long.parseLong(obj.toString());
        }catch (Exception e){
            return _default;
        }
    }

    public static int getTerminal(HttpServletRequest request){
        String userAgent = getUserAgent(request);
        int terminal = 0;
        if(userAgent != null){
            if(userAgent.contains("YZL ANDROID")){
                terminal = 1;
            }else if(userAgent.contains("YZL IOS")){
                terminal = 2;
            }else {
                terminal = 3;
            }
        }
        return terminal;
    }

    public static String getUserAgent(HttpServletRequest request){
        //获取终端
        if(request == null){
            return null;
        }
        String userAgent = request.getHeader("User-Agent");
        return userAgent;
    }
}
