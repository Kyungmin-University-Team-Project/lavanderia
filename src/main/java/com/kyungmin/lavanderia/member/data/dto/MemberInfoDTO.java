package com.kyungmin.lavanderia.member.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDTO {

    private String memberId; // 멤버 아이디
    private String memberName; // 멤버 이름
    private String memberEmail; // 멤버 이메일
    private String memberPhone; // 멤버 전화번호
    private String memberBirth; // 멤버 생일
    private int memberLevel; // 멤버 레벨
    private int memberPoint; // 멤버 포인트
    private String agreeMarketingYn;  // 마케팅 동의 여부
    private String memberProfileImg; // 멤버 프로필 이미지
}
