package com.kyungmin.lavanderia.global.auth.util;

import com.kyungmin.lavanderia.global.auth.jwt.data.entity.RefreshEntity;
import com.kyungmin.lavanderia.global.auth.jwt.data.repository.RefreshRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final RefreshRepository refreshRepository;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret, RefreshRepository refreshRepository) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 적절한 Secret Key 생성
        this.refreshRepository = refreshRepository;
    }

    public String getMemberId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("memberId", String.class);
    }

    public List<String> getRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String roles = claims.get("roles", String.class);
        return Arrays.asList(roles.split(","));
    }

    public Boolean isExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public String getCategory(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("category", String.class);
    }

    public String createJwt(String category, String memberId, List<String> roles, Long expiredMs) {
        String rolesStr = String.join(",", roles);

        return Jwts.builder()
                .claim("category", category)
                .claim("memberId", memberId)
                .claim("roles", rolesStr)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS256) // Secret Key와 알고리즘 명시
                .compact();
    }

    public void addRefreshEntity(String memberId, String refresh, Long expiredMs, String ipAddress) {
        RefreshEntity refreshEntity = RefreshEntity.builder()
                .memberId(memberId + ":" + ipAddress)
                .refresh(refresh)
                .expiration(expiredMs / 1000)
                .build();

        refreshRepository.save(refreshEntity);
    }
}
