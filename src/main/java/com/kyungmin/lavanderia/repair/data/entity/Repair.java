package com.kyungmin.lavanderia.repair.data.entity;

import com.kyungmin.lavanderia.member.data.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_REPAIR")
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPAIR_ID")
    private Long repairId;

    // Member와 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    // 옷 종류
    @Column(name = "CLOTHES_TYPE")
    private String clothesType;

    // 어떻게
    @Column(name = "HOW_TO")
    private String howTo;

    // 상세 정보
    @Column(name = "DETAIL_INFO")
    private String detailInfo;

    // 요청 사항
    @Column(name = "REQUEST")
    private String request;

    // 가격
    @Column(name = "PRICE")
    private int price;
}