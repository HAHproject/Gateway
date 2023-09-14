package com.example.reservationgateway.config;

import com.example.reservationgateway.entity.User;
import com.example.reservationgateway.security.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;


    public TokenInfo extractUser(String token){
        Claims claims = extractAllClaims(token);
        return TokenInfo.builder()
                .id(UUID.fromString(claims.get("id", String.class)))
                .username(claims.get("username", String.class))
                .email(claims.get("email", String.class))
                .role(Role.valueOf(claims.get("role", String.class)))
                .build();
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            User user
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public String generateToken(
            User user
    ) {
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", user.getId());
        extraClaims.put("username", user.getUsername());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("role", user.getRole().name());
        return generateToken(extraClaims, user);
    }

    private Claims extractAllClaims(String token) {
        return (Claims) Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parse(token)
                .getBody();

    }
}
