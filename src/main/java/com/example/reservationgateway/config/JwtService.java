package com.example.reservationgateway.config;

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
                .name(claims.get("username", String.class))
//                .email(claims.get("email", String.class))
//                .role(Role.valueOf(claims.get("role", String.class)))
                .build();
    }



    private Claims extractAllClaims(String token) {
        return (Claims) Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parse(token)
                .getBody();

    }
}
