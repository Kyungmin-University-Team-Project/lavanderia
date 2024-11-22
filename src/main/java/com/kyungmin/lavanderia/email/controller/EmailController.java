package com.kyungmin.lavanderia.email.controller;

import com.kyungmin.lavanderia.email.data.dto.EmailDTO;
import com.kyungmin.lavanderia.email.service.EmailService;
import com.kyungmin.lavanderia.member.exception.EmailSendFailedEx;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "email API")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-signup-code")
    @Operation(summary = "회원가입 이메일 전송", description = "회원가입 이메일 인증코드를 전송합니다<br>" +
            "json 형식으로 전송<br>email: 이메일 주소")
    public ResponseEntity<String> sendSignupCode(@RequestBody EmailDTO email) {
        try {
            emailService.sendSignupCode(email.getEmail());
            return ResponseEntity.ok("이메일 전송 성공");
        } catch (EmailSendFailedEx e) {
            log.error("회원가입 이메일 인증번호 전송 실패" + e.getMessage());
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }
    }

    @PostMapping("/send-id-code")
    @Operation(summary = "아이디 찾기 인증번호 이메일 발송", description = "아이디 찾기 시 이메일로 인증번호 전송<br>" +
            "json 형식으로 전송<br>email: 이메일 주소")
    public ResponseEntity<String> sendIDCode(@RequestBody EmailDTO email) {
        try {
            emailService.sendIdPwCode(email.getEmail(), "ID");
            return ResponseEntity.ok("이메일 전송 성공");
        } catch (EmailSendFailedEx e) {
            log.error("아이디 찾기 인증번호 이메일 전송 실패" + e.getMessage());
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }
    }

    @PostMapping("/send-pw-code")
    @Operation(summary = "비밀번호 찾기 인증번호 이메일 발송", description = "아이디 찾기 시 이메일로 인증번호 전송<br>" +
            "json 형식으로 전송<br>email: 이메일 주소")
    public ResponseEntity<String> sendPWCode(@RequestBody EmailDTO email) {
        try {
            emailService.sendIdPwCode(email.getEmail(), "PW");
            return ResponseEntity.ok("이메일 전송 성공");
        } catch (EmailSendFailedEx e) {
            log.error("비밀번호 찾기 인증번호 이메일 전송 실패" + e.getMessage());
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }
    }

}
