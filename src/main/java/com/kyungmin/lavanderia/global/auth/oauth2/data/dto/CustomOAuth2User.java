package com.kyungmin.lavanderia.global.auth.oauth2.data.dto;

import com.kyungmin.lavanderia.member.data.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User{

    private final MemberEntity memberEntity;

    public CustomOAuth2User(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return memberEntity.getMemberRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return memberEntity.getMemberId();
    }

    public String getUsername() {

        return memberEntity.getMemberName();
    }
}
