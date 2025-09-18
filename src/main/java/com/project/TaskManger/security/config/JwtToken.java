package com.project.TaskManger.security.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtToken {
    // Replace this with your actual secret key
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public  String generateToken(String username, long expirationMillis) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMillis);

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        Map<String, Object> claims = new HashMap<>();
        // You can add custom claims here if needed

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
