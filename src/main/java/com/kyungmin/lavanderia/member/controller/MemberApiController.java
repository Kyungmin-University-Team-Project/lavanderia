package com.kyungmin.lavanderia.member.controller;

import com.kyungmin.lavanderia.email.data.dto.CheckTokenDTO;
import com.kyungmin.lavanderia.email.service.EmailService;
import com.kyungmin.lavanderia.member.data.dto.MemberDTO;
import com.kyungmin.lavanderia.member.data.dto.MemberInfoDTO;
import com.kyungmin.lavanderia.member.data.dto.SignupDTO;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.member.exception.DuplicateMemberIdEx;
import com.kyungmin.lavanderia.member.exception.DuplicatePhoneNumberEx;
import com.kyungmin.lavanderia.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "회원 API")
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final EmailService emailService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "비활성화 상태로 회원 정보를 등록합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "회원가입 실패", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> signup(@RequestBody SignupDTO signupDto) {
        try {
            memberService.signup(signupDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패");
        }
    }

    @PostMapping("/member-info")
    @Operation(summary = "회원 정보 확인", description = "토큰을 주면 회원 정보를 반환합니다")
    public ResponseEntity<MemberInfoDTO> member(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(memberService.memberInfo(member));
    }

    @PostMapping("/member-pwd")
    @Operation(summary = "비밀번호 변경", description = "토큰 및 새로운 비밀번호를 주면 비밀번호를 변경합니다")
    public ResponseEntity<String> memberPwd(@AuthenticationPrincipal Member member, @RequestBody MemberDTO.ChangePasswordDTO request) {

        try {
            memberService.updateMemberPwd(member, request);
            return ResponseEntity.ok("비밀번호 변경 완료");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 프로필 이미지 업데이트
    @PostMapping("/member-profile")
    @Operation(summary = "프로필 이미지 업데이트", description = "토큰 및 프로필 이미지를 주면 프로필 이미지를 업데이트합니다<br>기본 이미지로 변경하려면 profileImg값을 null로 보내주세요<br>" +
            "multipart/form-data 형식으로 전송, 토큰 필요<br>profileImg: 프로필 이미지")
    public ResponseEntity<String> memberProfile(@AuthenticationPrincipal Member member,@RequestParam(value = "profileImg", required = false) MultipartFile profileImg) {
        try {
            memberService.updateMemberProfile(member, profileImg);
            return ResponseEntity.ok("프로필 이미지 업데이트 완료");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/member-delete")
    @Operation(summary = "회원 삭제", description = "토큰을 주면 회원을 삭제합니다.")
    public ResponseEntity<String> memberDelete(@AuthenticationPrincipal Member member) {
        try {
            memberService.memberDelete(member);
            return ResponseEntity.ok("회원 삭제 완료");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify-signup-code")
    @Operation(summary = "이메일 인증코드 검사", description = "이메일 인증코드를 검사하고, 인증 성공시 회원을 활성화합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이메일 인증코드 인증 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "이메일 인증코드 인증 실패", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> checkSignupCode(@RequestBody CheckTokenDTO checkTokenDTO) {
        try {
            memberService.checkSignupCode(checkTokenDTO.getEmail(), checkTokenDTO.getToken());
            return ResponseEntity.status(HttpStatus.CREATED).body("이메일 인증코드 인증 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("이메일 인증코드 인증 실패");
        }
    }

    @PostMapping("/verify-id-code")
    @Operation(summary = "아이디 찾기 이메일 인증번호 검사", description = "아이디 찾기 코드 입력 시 인증코드를 검사하고, 인증 성공시 회원 ID를 반환합니다")
    public ResponseEntity<String> checkIDCode(@RequestBody CheckTokenDTO checkTokenDTO) {
        try {
            emailService.checkIdPwCode(checkTokenDTO.getEmail(), checkTokenDTO.getToken(), "ID");
            System.out.println("checkTokenDTO.getEmail() = " + checkTokenDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(memberService.findMemberByEmail(checkTokenDTO.getEmail()).getMemberId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("이메일 인증코드 인증 실패" + e.getMessage());
        }
    }

    @PostMapping("/verify-pw-code")
    @Operation(summary = "비밀번호 찾기 이메일 인증번호 검사", description = "비밀번호 찾기 코드 입력 시 인증코드를 검사하고, 인증 성공시 회원 201을 반환합니다")
    public ResponseEntity<String> checkPWCode(@RequestBody CheckTokenDTO checkTokenDTO) {
        try {
            emailService.checkIdPwCode(checkTokenDTO.getEmail(), checkTokenDTO.getToken(), "PW");
            return ResponseEntity.status(HttpStatus.CREATED).body("이메일 인증코드 인증 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("이메일 인증코드 인증 실패");
        }
    }

    @PostMapping("/check-member-id")
    @Operation(summary = "멤버 아이디 중복 확인", description = "String 타입의 아이디를 받아 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 가입 가능", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "아이디 가입 불가능", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> checkMemberId(@RequestBody String memberId) {
        try {
            memberService.checkMemberId(memberId);
            return ResponseEntity.ok("가입 가능한 아이디입니다");
        } catch (DuplicateMemberIdEx e) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다");
        }
    }

    @PostMapping("/check-phone-number")
    @Operation(summary = "전화번호 중복 확인", description = "String 타입의 전화번호를 받아 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전화번호 가입 가능", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "전화번호 가입 불가능", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> checkPhoneNumber(@RequestBody String phoneNumber) {
        try {
            memberService.checkPhoneNumber(phoneNumber);
            return ResponseEntity.ok("가입 가능한 번호입니다");
        } catch (DuplicatePhoneNumberEx e) {
            return ResponseEntity.badRequest().body("이미 존재하는 번호입니다");
        }
    }

    @PostMapping("/check-email")
    @Operation(summary = "이메일 중복 확인", description = "String 타입의 이메일을 받아 중복을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 가입 가능", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "이메일 가입 불가능", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> checkEmail(@RequestBody String email) {
        try {
            memberService.checkEmail(email);
            return ResponseEntity.ok("가입 가능한 이메일입니다");
        } catch (DuplicatePhoneNumberEx e) {
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다");
        }
    }
}
