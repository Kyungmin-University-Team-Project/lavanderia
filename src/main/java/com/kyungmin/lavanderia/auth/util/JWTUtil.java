package com.kyungmin.lavanderia.auth.util;

import com.kyungmin.lavanderia.auth.jwt.data.entity.RefreshEntity;
import com.kyungmin.lavanderia.auth.jwt.data.repository.RefreshRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class JWTUtil {

    private SecretKey secretKey;
    private final RefreshRepository refreshRepository;


    public JWTUtil(@Value("${spring.jwt.secret}") String secret, RefreshRepository refreshRepository) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshRepository = refreshRepository;
    }

    public String getMemberId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberId", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String createJwt(String category, String memberId, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category)
                .claim("memberId", memberId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public void addRefreshEntity(String memberId, String refresh, Long expiredMs) {
        // 한국 시간대로 설정
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년MM월dd일 HH:mm:ss");
        dateFormat.setTimeZone(timeZone);

        Date date = new Date(System.currentTimeMillis() + expiredMs);
        String formattedDate = dateFormat.format(date);

        RefreshEntity refreshEntity = RefreshEntity.builder()
                .memberId(memberId + ":" + formattedDate)
                .refresh(refresh)
                .expiration(expiredMs / 1000)
                .build();

        refreshRepository.save(refreshEntity);
    }
}
