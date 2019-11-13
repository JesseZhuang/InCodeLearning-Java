package github.incodelearning.security;


import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JJWTUtils {
    private static String secret = "ASIAYBOE44JEMIYPM"; // private key secret distributed
    public static String create(String type, String subject, long ttlMillis) {
        SignatureAlgorithm alg = SignatureAlgorithm.HS256;
        long start = System.currentTimeMillis();
        Date now = new Date(start);
        Map<String, Object> claims = new HashMap<>();
        claims.put("old", "b03221-35da-8fab-cef889");
        claims.put("org", "jessezhuang.github.io");
        SecretKey secretKey = key(secret);
        JwtBuilder builder = Jwts.builder().setClaims(claims).setId(type).setIssuedAt(now).setSubject(subject)
                .signWith(alg, secretKey);
        if (ttlMillis > 0) {
            long expMillis = start + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    private static SecretKey key(String secret) {
        byte[] encodeKey = Base64Codec.BASE64.decode(secret);
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
    }
}
