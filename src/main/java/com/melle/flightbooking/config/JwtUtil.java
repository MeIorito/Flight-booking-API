package com.melle.flightbooking.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    //Claims will look like this:
    // "sub": "email",
    // "role": "role",
    // "iat": 1710000000,
    // "exp": 1710001800

    // Remove from here into env file
    @Value("${jwt.secret}")  // (1)
    private String secret;

    public String createToken(Map<String, Object> claims, String email){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Helper for signing
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims extractClaims(String jwtToken){
        return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
    }

    public String extractSub(String jwtToken){
        return extractClaims(jwtToken).getSubject();
    }

    public String extractRole(String jwtToken){
        return extractClaims(jwtToken).get("role", String.class);
    }

    public Integer extractId(String jwtToken){
        return extractClaims(jwtToken).get("id", Integer.class);
    }

    public Date extractExpirationDate(String jwtToken){
        return extractClaims(jwtToken).getExpiration();
    }

    public Boolean isTokenExpired(String jwtToken){
        return (extractExpirationDate(jwtToken).before(new Date()));
    }

    public Boolean validateToken(String jwtToken){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(jwtToken);

            return !isTokenExpired(jwtToken);

        } catch (Exception e){
            return false;
        }
    }
}
