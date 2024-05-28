package com.kyungmin.lavanderia.member.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
@Table(name = "TBL_MEMBER")
public class MemberEntity implements UserDetails {

    @Id
    @Column(name = "MEMBER_ID")
    private String memberId; // 멤버 아이디

    @Column(name = "MEMBER_PWD")
    private String memberPwd; // 멤버 비밀번호

    @Column(name = "MEMBER_NAME")
    private String memberName; // 멤버 이름

    @Column(name = "MEMBER_EMAIL")
    private String memberEmail; // 멤버 이메일

    @Column(name = "MEMBER_ROLE")
    private String memberRole; // 멤버 권한

    @Column(name = "MEMBER_PHONE")
    private String memberPhone; // 멤버 전화번호

    @Column(name = "MEMBER_BIRTHDAY")
    private Date memberBirthday; // 멤버 생일

    @Column(name = "MEMBER_LEVEL")
    private String memberLevel; // 멤버 레벨

    @Column(name = "MEMBER_POINT")
    private String memberPoint; // 멤버 포인트

    @Column(name = "AGREE_MARKETING_YN")
    private String agreeMarketingYn;  // 마케팅 동의 여부

    @Column(name = "ACC_INACTIVE_YN")
    private String accInactiveYn;    // 계정 비활성화 여부

    @Column(name = "TEMP_PWD_YN")
    private String tempPwdYn; // 임시 비밀번호 여부

    @Column(name = "ACC_LOGIN_COUNT")
    private long accLoginCount; // 누적 로그인 횟수

    @Column(name = "LOGIN_FAIL_COUNT")
    private long loginFailCount;  // 로그인 실패 횟수

    @Column(name = "LAST_LOGIN_DATE")
    private Date lastLoginDate;   // 최근 로그인 일시

    @Column(name = "ACC_REGISTER_DATE")
    private Date accRegisterDate;    // 계정 등록 일시

    @Column(name = "ACC_UPDATE_DATE")
    private Date accUpdateDate;   // 계정 수정 일시

    @Column(name = "ACC_DELETE_DATE")
    private Date accDeleteDate;   // 계정 삭제 일시


    @Builder
    public MemberEntity(String memberId, String memberPwd, String memberName,String memberEmail, String memberPhone, String agreeMarketingYn,String memberRole,Date memberBirthday){
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberPhone = memberPhone;
        this.memberBirthday = memberBirthday;
        this.memberRole = memberRole;
        this.agreeMarketingYn = agreeMarketingYn;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberRole;
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return memberPwd;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
