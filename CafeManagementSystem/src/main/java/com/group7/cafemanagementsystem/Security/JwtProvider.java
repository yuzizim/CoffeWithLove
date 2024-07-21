package com.group7.cafemanagementsystem.Security;

import com.group7.cafemanagementsystem.model.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();
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

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValuesByName(request, JwtConstants.JWT_COOKIE_NAME);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValuesByName(request, JwtConstants.REFRESH_TOKEN_COOKIE_NAME);
    }

    public String getCookieValuesByName(HttpServletRequest request, String name) {
        String token = null;
        if (request.getCookies() != null) {
            Cookie[] rc = request.getCookies();
            for (int i = 0; i < rc.length; i++) {
                if (rc[i].getName().equals(name) == true) {
                    token = rc[i].getValue().toString();
                }
            }
        }
        return token;
    }

    public ResponseCookie generateJwtCookie(Account account) {
        String jwt = generateTokenFromUsername(account.getUserName());
        return generateCookie(JwtConstants.JWT_COOKIE_NAME, jwt, "/");
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JwtConstants.EXPIRE_DATE))
                .signWith(keys(), SignatureAlgorithm.HS256)
                .compact();
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, refreshToken, "/auth/refresh-token");
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        ResponseCookie responseCookie = ResponseCookie.from(name, value)
                .path(path)
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .build();
        return responseCookie;
    }

    // Method to convert ResponseCookie to Cookie and add to response
    public void addCookieToResponse(HttpServletResponse response, ResponseCookie responseCookie) {
        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setPath(responseCookie.getPath());
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setSecure(responseCookie.isSecure());
        response.addCookie(cookie);
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(JwtConstants.JWT_COOKIE_NAME, null).maxAge(0).path("/").build();
        return cookie;
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        ResponseCookie cookie = ResponseCookie.from(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, null).maxAge(0).path("/auth/refresh-token").build();
        return cookie;
    }
}
