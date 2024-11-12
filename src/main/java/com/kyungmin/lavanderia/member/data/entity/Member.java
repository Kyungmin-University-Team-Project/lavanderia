package com.kyungmin.lavanderia.member.data.entity;

import com.kyungmin.lavanderia.address.data.entity.Address;
import com.kyungmin.lavanderia.cart.data.entity.Cart;
import com.kyungmin.lavanderia.order.data.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_MEMBER")
public class Member implements UserDetails {

    @Id
    @Column(name = "MEMBER_ID")
    private String memberId; // 멤버 아이디

    @Column(name = "MEMBER_PWD")
    private String memberPwd; // 멤버 비밀번호

    @Column(name = "MEMBER_NAME")
    private String memberName; // 멤버 이름

    @Column(name = "MEMBER_EMAIL")
    private String memberEmail; // 멤버 이메일

    @Column(name = "MEMBER_PHONE")
    private String memberPhone; // 멤버 전화번호

    @Column(name = "MEMBER_BIRTH")
    private LocalDate memberBirth; // 멤버 생일

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
    private LocalDateTime lastLoginDate;   // 최근 로그인 일시

    @Column(name = "ACC_REGISTER_DATE", updatable = false)
    private LocalDateTime accRegisterDate;    // 계정 등록 일시

    @Column(name = "ACC_UPDATE_DATE")
    private LocalDateTime accUpdateDate;   // 계정 수정 일시

    @Column(name = "ACC_DELETE_DATE")
    private LocalDateTime accDeleteDate;   // 계정 삭제 일시


    @OneToMany(mappedBy = "memberId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL)
    List<Address> address; // 주소

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL)
    List<Cart> cart; // 장바구니

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL)
    List<Order> order; // 주문

    /*@Builder
    public Member(String memberId, String memberPwd, String memberName, String memberEmail, String memberPhone, String agreeMarketingYn, List<Role> roles, LocalDate memberBirth, String memberLevel,
                  String memberPoint, String accInactiveYn, String tempPwdYn, long accLoginCount, long loginFailCount, LocalDateTime lastLoginDate, LocalDateTime accRegisterDate, LocalDateTime accUpdateDate, LocalDateTime accDeleteDate){
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberPhone = memberPhone;
        this.agreeMarketingYn = agreeMarketingYn;
        this.memberBirth = memberBirth;
        this.memberLevel = (memberLevel != null) ? memberLevel : "1";
        this.memberPoint = (memberPoint != null) ? memberPoint : "0";
        this.accInactiveYn = (accInactiveYn != null) ? accInactiveYn : "N";
        this.tempPwdYn = (tempPwdYn != null) ? tempPwdYn : "N";
        this.accLoginCount = accLoginCount;
        this.loginFailCount = loginFailCount;
        this.lastLoginDate = lastLoginDate;
        this.accRegisterDate = accRegisterDate;
        this.accUpdateDate = accUpdateDate;
        this.accDeleteDate = accDeleteDate;
        this.roles = roles;
    }*/

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        accRegisterDate = now;
        accUpdateDate = now;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthorities()));
        }
         return authorities;
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
        return !accInactiveYn.equals("Y");
    }

}
