package com.kyungmin.lavanderia.member.data.entity;

import com.kyungmin.lavanderia.address.data.entity.Address;
import com.kyungmin.lavanderia.cart.data.entity.Cart;
import com.kyungmin.lavanderia.lifeLaundry.data.entity.LifeLaundryCart;
import com.kyungmin.lavanderia.order.data.entity.Order;
import com.kyungmin.lavanderia.repair.data.entity.Repair;
import com.kyungmin.lavanderia.repair.data.entity.RepairCart;
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
    private int memberLevel = 1; // 멤버 레벨

    @Column(name = "MEMBER_POINT")
    private int memberPoint = 0; // 멤버 포인트

    @Column(name = "AGREE_MARKETING_YN")
    private String agreeMarketingYn;  // 마케팅 동의 여부

    @Column(name = "ACC_INACTIVE_YN")
    private String accInactiveYn = "N";    // 계정 비활성화 여부

    @Column(name = "TEMP_PWD_YN")
    private String tempPwdYn = "N"; // 임시 비밀번호 여부

    @Column(name = "ACC_LOGIN_COUNT")
    private int accLoginCount = 0; // 누적 로그인 횟수

    @Column(name = "LOGIN_FAIL_COUNT")
    private int loginFailCount = 0;  // 로그인 실패 횟수

    @Column(name = "LAST_LOGIN_DATE")
    private LocalDateTime lastLoginDate;   // 최근 로그인 일시

    @Column(name = "ACC_REGISTER_DATE", updatable = false)
    private LocalDateTime accRegisterDate;    // 계정 등록 일시

    @Column(name = "ACC_UPDATE_DATE")
    private LocalDateTime accUpdateDate;   // 계정 수정 일시

    @Column(name = "ACC_DELETE_DATE")
    private LocalDateTime accDeleteDate;   // 계정 삭제 일시

    @Column(name = "PROFILE_IMG")
    private String memberProfileImg = "default"; // 프로필 이미지


    @OneToMany(mappedBy = "memberId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL)
    List<Address> address; // 주소

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL)
    List<Cart> cart; // 장바구니

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL)
    List<Order> order; // 주문

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Repair> repairs = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepairCart> repairCarts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LifeLaundryCart> lifeLaundryCarts = new ArrayList<>();

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
