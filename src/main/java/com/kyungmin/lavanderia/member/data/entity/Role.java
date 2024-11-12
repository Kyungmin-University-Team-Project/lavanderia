package com.kyungmin.lavanderia.member.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long roleId; // 권한 아이디

    @Column(name = "AUTHORITIES")
    private String authorities; // 권한

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member memberId;

}
