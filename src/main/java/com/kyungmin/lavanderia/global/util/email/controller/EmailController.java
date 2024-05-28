package com.kyungmin.lavanderia.global.util.email.controller;

import com.kyungmin.lavanderia.global.util.email.service.EmailService;
import com.kyungmin.lavanderia.member.data.dto.CheckCodeDTO;
import com.kyungmin.lavanderia.member.exception.EmailAuthenticationFailedEx;
import com.kyungmin.lavanderia.member.exception.EmailSendFailedEx;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "email API")
public class EmailController {

    private final EmailService emailService;


    @PostMapping("/send-signup-code")
    @Operation(summary = "이메일 인증코드 전송", description = "이메일 인증코드를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이메일 인증코드 전송 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "이메일 인증코드 전송 실패", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> sendSignupCode(@RequestBody String email) {

        HttpStatus httpStatus;
        String result;

        try {
            emailService.sendSignupCode(email);
            httpStatus = HttpStatus.CREATED;
            result = "이메일 전송 완료";
        } catch (EmailSendFailedEx e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = "이메일 전송 실패";
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result = "예상치 못한 오류가 발생했습니다";
        }

        return response(httpStatus, result);
    }

    @GetMapping("/verify-signup-code")
    @Operation(summary = "이메일 인증코드 검사", description = "이메일 인증코드를 검사합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이메일 인증코드 인증 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "이메일 인증코드 인증 실패", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> checkSignupCode(@ModelAttribute CheckCodeDTO checkCodeDTO) {

        HttpStatus httpStatus;
        String result;

        try {
            emailService.checkSignupCode(checkCodeDTO.getEmail(), checkCodeDTO.getToken());
            httpStatus = HttpStatus.CREATED;
            result = "이메일 인증 성공";
        } catch (EmailAuthenticationFailedEx e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = "이메일 인증 실패";
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            result = "예상치 못한 오류가 발생했습니다";
        }

        return response(httpStatus, result);
    }

    private ResponseEntity<String> response(HttpStatus httpStatus,String result) {
        return ResponseEntity.status(httpStatus)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
                .body(result);
    }
}
