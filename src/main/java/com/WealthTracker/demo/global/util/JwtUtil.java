package com.WealthTracker.demo.global.util;

import com.WealthTracker.demo.domain.auth.dto.CustomUserInfoDTO;
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
                .setSubject(String.valueOf(member.getUserId()))
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //** 토큰에서 userId 추출
    public Long getUserId(String token) {
        String accessToken = getAccessToken(token);
        return Long.valueOf(parseClaims(accessToken).getSubject());
    }

    // JWT 검증
    public boolean validationToken(String token) {
        String accessToken = getAccessToken(token);
        try {
            if (accessToken.trim().isEmpty()) {
                throw new JwtException("JWT가 비어 있습니다.");
            }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch (JwtException e) {
            log.info("유효하지 않은 JWT 토큰입니다.", e);
            return false;
        }
    }

    public Claims parseClaims(String token) {
        String accessToken = getAccessToken(token);
        try {
            if (accessToken.trim().isEmpty()) {
                throw new JwtException("JWT가 비어 있습니다.");
            }
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
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
