package com.kyungmin.lavanderia.member.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupDTO {

    @Schema(description = "회원 아이디", example = "user")
    private String memberId;
    @Schema(description = "회원 비밀번호", example = "123")
    private String memberPwd;
    @Schema(description = "회원 이름", example = "이용호")
    private String memberName;
    @Schema(description = "회원 이메일", example = "abc@naver.com")
    private String memberEmail;
    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    private String memberPhone;
    @Schema(description = "마케팅 동의 여부", example = "N")
    private String agreeMarketingYn;
    @Schema(description = "생일", example = "2000-05-11")
    private LocalDate memberBirth;


}
