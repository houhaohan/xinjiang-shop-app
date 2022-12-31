package com.pinet.core.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * @date Nov 20, 2018
 */
public class JwtTokenUtils implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	private static final String USERID = Claims.SUBJECT;
	/**
	 * 创建时间
	 */
	private static final String CREATED = "created";

	/**
     * 密钥
     */
    private static final String SECRET = "a9fd38419340ee10";

    /**
     * 有效期7天
     */
    public static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;


    /**
	 * 生成令牌
	 ** @return 令牌
	 */
	public static String generateToken(Long userId) {
	    Map<String, Object> claims = new HashMap<>(2);
	    claims.put(USERID, userId);
	    claims.put(CREATED, new Date());
	    return generateToken(claims);
	}

	/**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
	 * 从令牌中获取用户名
	 *
	 * @param token 令牌
	 * @return 用户名
	 */
	public static Long getUserIdFromToken(String token) {
	    String userId;
	    try {
	        Claims claims = getClaimsFromToken(token);
			userId = claims.getSubject();
			return Long.valueOf(userId);
	    } catch (Exception e) {

	    }
	    return null;
	}


	/**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
	 * 验证令牌
	 * @param token
	 * @param userId
	 * @return false 失效， true 未失效
	 */
	public static boolean validateToken(String token, Long userId) {
	    Long userid = getUserIdFromToken(token);
	    if(userid==null) return false;
	    return (userid.equals(userId) && !isTokenExpired(token));
	}

	/**
	 * 刷新令牌
	 * @param token
	 * @return
	 */
	public static String refreshToken(String token) {
	    String refreshedToken;
	    try {
	        Claims claims = getClaimsFromToken(token);
	        claims.put(CREATED, new Date());
	        refreshedToken = generateToken(claims);
	    } catch (Exception e) {
	        refreshedToken = null;
	    }
	    return refreshedToken;
	}

	/**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

	public static void main(String[] args) {
		String token = generateToken(3967L);
		System.out.println(token);

	}

}