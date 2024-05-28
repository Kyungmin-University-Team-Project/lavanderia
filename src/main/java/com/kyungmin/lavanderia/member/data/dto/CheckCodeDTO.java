package com.kyungmin.lavanderia.member.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckCodeDTO {
    @Schema(description = "회원 이메일", example = "user@naver.com", required = true)
    private String email;
    @Schema(description = "받은 인증 토큰", example = "uuid", required = true)
    private String token;
}
