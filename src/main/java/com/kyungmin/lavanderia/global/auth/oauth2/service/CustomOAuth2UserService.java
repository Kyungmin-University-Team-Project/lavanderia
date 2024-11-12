package com.kyungmin.lavanderia.global.auth.oauth2.service;

import com.kyungmin.lavanderia.global.auth.oauth2.data.dto.CustomOAuth2User;
import com.kyungmin.lavanderia.global.auth.oauth2.data.dto.GoogleResponse;
import com.kyungmin.lavanderia.global.auth.oauth2.data.dto.NaverResponse;
import com.kyungmin.lavanderia.global.auth.oauth2.data.dto.OAuth2Response;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.member.data.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        // naver인지 google인지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        Optional<Member> existData = memberRepository.findById(username);

        if (existData.isEmpty()) {

            Member member = Member.builder()
                    .memberId(username)
                    .memberName(oAuth2Response.getName())
                    .memberEmail(oAuth2Response.getEmail())
                    //.memberRole("ROLE_USER")
                    .build();


            memberRepository.save(member);

            return new CustomOAuth2User(member);
        }
        else {

            Member member = Member.builder()
                    .memberId(existData.get().getMemberId())
                    .memberName(oAuth2Response.getName())
                    .memberEmail(oAuth2Response.getEmail())
                    //.memberRole(existData.get().getMemberRole())
                    .build();

            memberRepository.save(member);

            return new CustomOAuth2User(member);
        }
    }

}
