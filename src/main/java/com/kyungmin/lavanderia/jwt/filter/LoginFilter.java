package com.kyungmin.lavanderia.jwt.filter;

import com.kyungmin.lavanderia.jwt.util.JWTUtil;
import com.kyungmin.lavanderia.jwt.data.entity.RefreshEntity;
import com.kyungmin.lavanderia.jwt.data.repository.RefreshRepository;
import com.kyungmin.lavanderia.jwt.util.TokenExpirationTime;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private Long accessExpiredMs = TokenExpirationTime.ACCESS_TIME;
    private Long refreshExpiredMs = TokenExpirationTime.REFRESH_TIME;

    private final RefreshRepository refreshRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String memberId = obtainUsername(request);
        String memberPwd = obtainPassword(request);

        System.out.println(memberId);

        //스프링 시큐리티에서 memberId과 memberPwd를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberId, memberPwd, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //유저 정보
        String memberId = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", memberId, role, accessExpiredMs);
        String refresh = jwtUtil.createJwt("refresh", memberId, role, refreshExpiredMs);

        // Refresh 토큰 저장
        addRefreshEntity(memberId,refresh,  refreshExpiredMs);

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String memberId, String refresh, Long expiredMs) {

        RefreshEntity refreshEntity = RefreshEntity.builder()
                .memberId(memberId)
                .refresh(refresh)
                .expiration(expiredMs)
                .build();

        refreshRepository.save(refreshEntity);
    }
}