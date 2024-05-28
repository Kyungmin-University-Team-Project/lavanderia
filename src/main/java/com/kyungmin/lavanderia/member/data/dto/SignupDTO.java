package com.kyungmin.lavanderia.member.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupDTO {

    @Schema(description = "회원 아이디", example = "user", required = true)
    private String memberId;
    @Schema(description = "회원 비밀번호", example = "123", required = true)
    private String memberPwd;
    @Schema(description = "회원 이름", example = "이용호", required = true)
    private String memberName;
    @Schema(description = "회원 이메일", example = "abc@naver.com", required = true)
    private String memberEmail;
    @Schema(description = "회원 전화번호", example = "010-1234-5678", required = true)
    private String memberPhone;
    @Schema(description = "생일", example = "1990-05-20", required = true)
    private Date memberBirth;
    @Schema(description = "주소", example = "경기도 고양시 ㅇㅇ로 55", required = true)
    private String address;
    @Schema(description = "상세 주소", example = "101동 101호", required = true)
    private String detailAddress;
    @Schema(description = "마케팅 동의 여부", example = "N", required = true)
    private String agreeMarketingYn;

}
