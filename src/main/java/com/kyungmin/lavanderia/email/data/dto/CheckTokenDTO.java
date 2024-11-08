package com.kyungmin.lavanderia.email.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckTokenDTO {
    @Schema(description = "회원 이메일", example = "user@naver.com")
    private String email;
    @Schema(description = "받은 인증 token", example = "187623")
    private String token;
}
