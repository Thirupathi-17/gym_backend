package com.thirupathi.gym.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parser;

@Component
public class JwtProvider {

    static final String JWT_SECRET  = "d4523452ersdwerwesdgswetwer&$*#)$^#)*$^#)*^@#)$sdfry2342036204387";

    SecretKey key = Keys.hmacShaKeyFor( JWT_SECRET.getBytes());

    public String generatedToken(String userName, String role){
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration( new Date( new Date().getTime() + 86400000))
                .expiration( new Date( new Date().getTime() + 86400000))
                .setSubject( userName )
                .claim("role", role)
                .signWith(key)
                .compact();
    }

    public String getUsername(String token){
        return parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getBody()
                .getSubject();
    }


//    public Claims getPayLoad(String token) {
//
//        if (token == null) {
//            throw new RuntimeException("Token is NULL");
//        }
//
//        // ✅ Remove Bearer, spaces, and new lines completely
//        token = token
//                .replace("Bearer", "")
//                .replaceAll("\\s+", "")   // removes ALL whitespace
//                .trim();
//
//        System.out.println("CLEAN TOKEN = [" + token + "]");  // ✅ DEBUG
//
//        if (token.isEmpty()) {
//            throw new RuntimeException("Token is EMPTY after cleaning");
//        }
//
//        return Jwts.parser()
//                .verifyWith(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }


    public Claims getPayLoad(String token){
       return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
