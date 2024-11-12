package com.kyungmin.lavanderia.member.controller;

import com.kyungmin.lavanderia.global.util.email.data.dto.CheckTokenDTO;
import com.kyungmin.lavanderia.member.data.dto.MemberInfoDTO;
import com.kyungmin.lavanderia.member.data.dto.SignupDTO;
import com.kyungmin.lavanderia.member.exception.DuplicateMemberIdEx;
import com.kyungmin.lavanderia.member.exception.DuplicatePhoneNumberEx;
import com.kyungmin.lavanderia.member.exception.EmailAuthenticationFailedEx;
import com.kyungmin.lavanderia.member.service.MemberService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 API")
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "비활성화 상태로 회원 정보를 등록합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "회원가입 실패", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> signup(@RequestBody SignupDTO signupDto) {

        HttpStatus httpStatus;
        String result;

        try {
            memberService.signup(signupDto);
            httpStatus = HttpStatus.CREATED;
            result = "회원가입 성공";
        } catch (Exception e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = "회원가입 실패";
        }

        return response(httpStatus, result);
    }

    @PostMapping("/verify-signup-code")
    @Operation(summary = "이메일 인증코드 검사", description = "이메일 인증코드를 검사하고, 인증 성공시 회원을 활성화합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이메일 인증코드 인증 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "이메일 인증코드 인증 실패", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> checkSignupCode(@RequestBody CheckTokenDTO checkTokenDTO) {

        HttpStatus httpStatus;
        String result;

        try {
            memberService.checkSignupCode(checkTokenDTO.getEmail(), checkTokenDTO.getToken());
            httpStatus = HttpStatus.CREATED;
            result = "로그인 후 이용해주세요";
        } catch (EmailAuthenticationFailedEx e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = "이메일 인증 실패";
        } catch (RuntimeException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = e.getMessage();
        }

        return response(httpStatus, result);
    }

    @PostMapping("/member-info")
    @Operation(summary = "회원 정보 확인", description = "토큰을 주면 회원 정보를 반환합니다")
    public MemberInfoDTO memberInfo() {

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

        return memberService.memberInfo(memberId);
    }

    @PostMapping("/member-delete")
    @Operation(summary = "회원 삭제", description = "토큰을 주면 회원을 삭제합니다.")
    public ResponseEntity<String> memberDelete() {

        HttpStatus httpStatus;
        String result;

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            memberService.memberDelete(memberId);
            httpStatus = HttpStatus.OK;
            result = "회원 삭제 완료";
        } catch (UsernameNotFoundException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = e.getMessage();
        }

        return response(httpStatus, result);
    }

    @PostMapping("/check-member-id")
    @Operation(summary = "멤버 아이디 중복 확인", description = "String 타입의 아이디를 받아 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 가입 가능", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "아이디 가입 불가능", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> checkMemberId(@RequestBody String memberId) {

        HttpStatus httpStatus;
        String result;

        try {
            memberService.checkMemberId(memberId);
            httpStatus = HttpStatus.CREATED;
            result = "가입 가능한 아이디입니다";
        } catch (DuplicateMemberIdEx e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = "이미 존재하는 아이디입니다";
        }

        return response(httpStatus, result);
    }

    @PostMapping("/check-phone-number")
    @Operation(summary = "전화번호 중복 확인", description = "String 타입의 전화번호를 받아 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전화번호 가입 가능", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "전화번호 가입 불가능", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> checkPhoneNumber(@RequestBody String phoneNumber) {

        HttpStatus httpStatus;
        String result;

        try {
            memberService.checkPhoneNumber(phoneNumber);
            httpStatus = HttpStatus.OK;
            result = "가입 가능한 번호입니다";
        } catch (DuplicatePhoneNumberEx e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = "이미 존재하는 번호입니다";
        }

        return response(httpStatus, result);
    }

    @PostMapping("/check-email")
    @Operation(summary = "이메일 중복 확인", description = "String 타입의 이메일을 받아 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 가입 가능", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "이메일 가입 불가능", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> checkEmail(@RequestBody String email) {

        HttpStatus httpStatus;
        String result;

        try {
            memberService.checkEmail(email);
            httpStatus = HttpStatus.OK;
            result = "가입 가능한 이메일입니다";
        } catch (DuplicatePhoneNumberEx e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = "이미 존재하는 이메일입니다";
        }

        return response(httpStatus, result);
    }

    private ResponseEntity<String> response(HttpStatus httpStatus,String result) {
        return ResponseEntity.status(httpStatus)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
                .body(result);
    }
}
