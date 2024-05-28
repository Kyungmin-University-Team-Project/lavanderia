package com.kyungmin.lavanderia.member.controller;

import com.kyungmin.lavanderia.member.data.dto.CheckCodeDTO;
import com.kyungmin.lavanderia.member.data.dto.SignupDTO;
import com.kyungmin.lavanderia.member.exception.DuplicateMemberIdEx;
import com.kyungmin.lavanderia.member.exception.DuplicatePhoneNumberEx;
import com.kyungmin.lavanderia.member.exception.EmailAuthenticationFailedEx;
import com.kyungmin.lavanderia.member.exception.EmailSendFailedEx;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원 API")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 완료", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> signup(@RequestBody SignupDTO signupDto) {

        memberService.signup(signupDto);

        HttpStatus httpStatus = HttpStatus.CREATED;
        String result = "회원 가입 완료";

        return response(httpStatus, result);
    }

    @PostMapping("/check-member-id")
    @Operation(summary = "아이디 중복 확인", description = "전화번호 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "아이디 가입 가능", content = @Content(mediaType = "application/json")),
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
    @Operation(summary = "전화번호 중복 확인", description = "전화번호 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "전화번호 가입 가능", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "전화번호 가입 불가능", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> checkPhoneNumber(@RequestBody String phoneNumber) {

        HttpStatus httpStatus;
        String result;

        try {
            memberService.checkPhoneNumber(phoneNumber);
            httpStatus = HttpStatus.CREATED;
            result = "가입 가능한 번호입니다";
        } catch (DuplicatePhoneNumberEx e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = "이미 존재하는 번호입니다";
        }

        return response(httpStatus, result);
    }

    private ResponseEntity<String> response(HttpStatus httpStatus,String result) {
        return ResponseEntity.status(httpStatus)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
                .body(result);
    }
}
