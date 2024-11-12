package com.kyungmin.lavanderia.global.auth.jwt.controller;

import com.kyungmin.lavanderia.global.auth.jwt.service.ReissueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReissueController {

    private final ReissueService reissueService;

    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }

    @PostMapping("/reissue")
    @Operation(summary = "refresh token 재발급", description = "쿠키에 refresh token을 담아서 전송해주세요. refresh token을 이용하여 access token을 재발급합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh token 재발급 성공"),
            @ApiResponse(responseCode = "400", description = "Refresh token 재발급 실패"),
    })
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        return reissueService.reissue(request, response);
    }
}
