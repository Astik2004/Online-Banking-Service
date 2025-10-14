package com.astik.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.astik.exception.JWTException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private final String SECRET_KEY = "thisIsAVerySecureAndLongSecretKey1234567890";
    private final long TOKEN_EXPIRY_DURATION = 15 * 60 *1000; // 15 mins

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String createToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRY_DURATION))
                .signWith(key)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenNotExpired(String token) {
        Date expiryDate = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiryDate.after(new Date());
    }

    public boolean validateToken(String token, String userId) {
        String userIdFromToken = getUserIdFromToken(token);
        if(userId.equals(userIdFromToken) && isTokenNotExpired(token))
        {
        	return true;
        }
        else
        {
            throw new JWTException("Token expired or invalid");
        }
    }
}
