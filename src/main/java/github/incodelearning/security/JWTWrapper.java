package github.incodelearning.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64Codec;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTWrapper {
    private static final String SECRET = "ASIAYBOE44JEMIYPM"; // private key SECRET
    public static String create(String id, String subject, long ttlMillis) {
        SignatureAlgorithm alg = SignatureAlgorithm.HS256;
        long start = System.currentTimeMillis();
        Date now = new Date(start);
        Map<String, Object> claims = new HashMap<>();
        claims.put("old", "b03221-35da-8fab-cef889");
        claims.put("org", "jessezhuang.github.io");
        SecretKey secretKey = key(SECRET);
        JwtBuilder builder = Jwts.builder().setClaims(claims).setId(id).setIssuedAt(now).setSubject(subject)
                .signWith(alg, secretKey);
        if (ttlMillis > 0) {
            long expMillis = start + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    private static SecretKey key(String secret) {
        // byte[] encodeKey = Base64Codec.BASE64.decode(secret);
        byte[] encodeKey = Base64.decodeBase64(secret.getBytes());
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
    }

    public static Claims parse(String jwt) {
        SecretKey secretKey = key(SECRET);
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
    }
}
