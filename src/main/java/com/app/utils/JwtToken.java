package com.app.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class JwtToken {
    /**
     * 公用密钥-保存在服务器，客户端不知道密钥，以防攻击
     */
    public static String SECRET = "Shirley";
    public static int lastTime = 60*24;//过期时间设定

    /**
     * token生成器
     * @return
     * @throws Exception
     */
    public static String generator(Integer uid) throws Exception{
        //签发时间
        Date iaDate = new Date();
        //过期时间：
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE,lastTime);
        Date expiresDate = nowTime.getTime();
        //设置header
        Map<String ,Object> header = new HashMap<>();
        header.put("alg","HS256");
        header.put("typ","JWT");
        String token = JWT.create()
                .withHeader(header)
                .withClaim("uid",uid)
                .withExpiresAt(expiresDate)
                .withIssuedAt(iaDate)
                .sign(Algorithm.HMAC256(SECRET));

        return token;
    }

    /**
     * 解密Token
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {
        JWTVerifier verifier = null;
        try {
            verifier = JWT.require(Algorithm.HMAC256(SECRET))
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        DecodedJWT jwt = null;
        jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    public static Claims decode(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims;
    }

    public static Integer getUid(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
//            Date date = claims.getExpiration();
            Integer name = (Integer) claims.get("uid");
//            return new Date().before(date);
            return name;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Optional<Claims> getMessage(String token) {
        Claims claims;
        claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return Optional.of(claims);
    }
}
