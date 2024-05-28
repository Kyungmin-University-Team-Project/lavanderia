package com.kyungmin.lavanderia.global.auth.oauth2.handler;

import com.kyungmin.lavanderia.global.auth.jwt.data.repository.RefreshRepository;
import com.kyungmin.lavanderia.global.auth.util.JWTUtil;
import com.kyungmin.lavanderia.global.auth.util.MakeCookie;
import com.kyungmin.lavanderia.global.auth.util.TokenExpirationTime;
import com.kyungmin.lavanderia.global.auth.oauth2.data.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private Long accessExpiredMs = TokenExpirationTime.ACCESS_TIME;
    private Long refreshExpiredMs = TokenExpirationTime.REFRESH_TIME;

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final MakeCookie makeCookie;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String memberId = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", memberId, role, accessExpiredMs);
        String refresh = jwtUtil.createJwt("refresh", memberId, role, refreshExpiredMs);

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        // Refresh 토큰 저장
        jwtUtil.addRefreshEntity(memberId, refresh, refreshExpiredMs, ipAddress);

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(makeCookie.createCookie("refresh", refresh));
        response.sendRedirect("http://localhost:3000/");

    }


}
