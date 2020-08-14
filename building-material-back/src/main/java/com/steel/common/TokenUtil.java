package com.steel.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Jwt;

import java.sql.SQLOutput;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: building-material
 * @Package: com.steel.common
 * @ClassName: TokenUtil
 * @Author: Hero
 * @Description: token验证
 * @Date: 2020/2/21 21:16
 * @Version: 1.0
 */
public class TokenUtil {

    // token有效时间 30分钟  单位毫秒
    private static final long EXPIRE_TIME = 30 * 60 * 1000;

    // token私匙
    private static final String TOKEN_SECRET = "building-material";

    /**
     * @Description 生成token值
     * @param id
     * @param password
     * @Return java.lang.String
     * @Exception
     */
    public static String getToken(String id, String password) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

        // 设置头部信息
        Map<String, Object> header = new HashMap<>(2);
        header.put("type", "JWT");
        header.put("algorithm", "HS256");
        return JWT.create()
                .withHeader(header)
                .withClaim("id",id)//在这里可以放自己需要放的数据
                .withClaim("password",password)
                .withExpiresAt(date)
                .sign(algorithm);
        //return JWT.create().withAudience(id).sign(Algorithm.HMAC256(password));
    }

    public static String getInfoFromToken(String token, String type) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            if (type.equals("password")) {
                return jwt.getClaim("password").asString();
            }
            if (type.equals("id")) {
                return jwt.getClaim("id").asString();
            }

        } catch (JWTDecodeException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
