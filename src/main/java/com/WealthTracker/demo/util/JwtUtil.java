package com.WealthTracker.demo.util;

import com.WealthTracker.demo.DTO.CustomUserInfoDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    /** Access Token 생성 **/
    public String createAccessToken(CustomUserInfoDTO member) {
        return createToken(member, accessTokenExpTime);
    }

    /** JWT 생성 **/
    private String createToken(CustomUserInfoDTO member, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("userId", member.getUserId());
        claims.put("email", member.getEmail());
        claims.put("name", member.getName());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(member.getEmail())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //** 토큰에서 userId 추출
    public String getUserId(Long token) {
        return parseClaims(String.valueOf(token)).getSubject();
    }

    // JWT 검증
    public boolean validationToken(String token) {
        try {
            if (token.trim().isEmpty()) {
                throw new JwtException("JWT is empty");
            }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.info("Invalid JWT Token", e);
            return false;
        }
    }

    public Claims parseClaims(String token) {
        try {
            if (token.trim().isEmpty()) {
                throw new JwtException("JWT is empty");
            }
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // JWT 토큰에서 "Bearer " 제거
    public String getAccessToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
