package com.group7.cafemanagementsystem.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();
        ;
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JwtConstants.EXPIRE_DATE);
        String token = Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(keys(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    private Key keys() {
        byte[] keyBytes = Decoders.BASE64.decode(JwtConstants.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keys())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public boolean vailidateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(keys())
                .build()
                .parseClaimsJws(token);
        return true;
    }
}
