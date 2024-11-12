package com.kyungmin.lavanderia.Inquiry.controller;

import com.kyungmin.lavanderia.Inquiry.data.dto.InquiryDto;
import com.kyungmin.lavanderia.Inquiry.service.InquiryService;
import com.kyungmin.lavanderia.member.data.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "문의 API")
@RequestMapping("/inquiry")
@RestController
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping("/insert")
    @Operation(summary = "새로운 주제의 최상위 문의 삽입", description = "새로운 주제의 문의를 생성할 때 사용")
    public ResponseEntity<String> insertInquiry(@AuthenticationPrincipal Member member,
                                                @RequestParam(value = "imageList", required = false) List<MultipartFile> imageList,
                                                @Valid @ModelAttribute InquiryDto.InquiryRequest inquiryRequest) {
        try {
            inquiryService.insertInquiry(member, imageList, inquiryRequest);
            return ResponseEntity.ok("문의 삽입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("입력 값 오류: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("문의 삽입 실패: 서버 오류 발생");
        }
    }

    @PostMapping("/detail/insert")
    @Operation(summary = "답변 or 추가 문의 삽입", description = "답변 또는 추가 문의 삽입")
    public ResponseEntity<String> insertInquiryDetail(@AuthenticationPrincipal Member member,
                                                      @RequestParam(value = "imageList", required = false) List<MultipartFile> imageList,
                                                      @Valid @ModelAttribute InquiryDto.InquiryDetailRequest request) {
        try {
            inquiryService.insertInquiryDetail(member, imageList, request);
            return ResponseEntity.ok(request.getType() + " 삽입 성공");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("권한 오류: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("입력 값 오류: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(request.getType() + " 삽입 실패: 서버 오류 발생");
        }
    }

    @PostMapping("/list/my")
    @Operation(summary = "회원별 최상위 문의 조회", description = "특정 회원의 최상위 문의를 조회")
    public ResponseEntity<?> getInquiriesByMember(@RequestBody InquiryDto.InquiriesByMemberRequestDTO request, @AuthenticationPrincipal Member member) {
        try {
            List<InquiryDto.InquiriesResponseDTO> inquiries = inquiryService.getInquiriesByMember(member, request);
            return ResponseEntity.ok(inquiries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("입력 값 오류: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("회원 문의 조회 실패: 서버 오류 발생");
        }
    }

    @PostMapping("")
    @Operation(summary = "문의 상세 조회", description = "특정 최상위 문의의 모든 대화와 답변 조회")
    public ResponseEntity<?> getInquiryDetail(@RequestBody Long inquiryId, @AuthenticationPrincipal Member member) {
        try {
            InquiryDto.InquiryDetailResponse responseList = inquiryService.getInquiryDetail(member, inquiryId);
            return ResponseEntity.ok(responseList);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("권한 오류: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 요청: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("문의 상세 조회 실패: 서버 오류 발생");
        }
    }

    @PostMapping("/delete")
    @Operation(summary = "문의 삭제", description = "문의와 관련된 모든 질문, 답변을 삭제합니다.")
    public ResponseEntity<String> deleteInquiry(@RequestBody Long inquiryId, @AuthenticationPrincipal Member member) {
        try {
            inquiryService.deleteInquiry(inquiryId, member);
            return ResponseEntity.ok("문의 삭제 성공");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("권한 오류: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 요청: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("문의 삭제 실패: 서버 오류 발생");
        }
    }

    @PostMapping("/list/status")
    @Operation(summary = "상태별 문의 리스트 조회", description = "상태에 따라 문의 목록을 조회합니다.")
    public ResponseEntity<?> getInquiriesByStatus(@RequestBody InquiryDto.InquiriesByStatusRequestDTO request) {
        try {
            List<InquiryDto.InquiriesResponseDTO> inquiries = inquiryService.getInquiriesByStatus(request);
            return ResponseEntity.ok(inquiries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 요청: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("상태별 문의 조회 실패: 서버 오류 발생");
        }
    }

    @PostMapping("/status/count")
    @Operation(summary = "상태별 전체 문의 카운트 조회", description = "상태별 전체 문의 카운트를 조회합니다.")
    public ResponseEntity<?> getInquiryStatusCount() {
        try {
            InquiryDto.InquiryStatusCountResponse inquiryStatusCount = inquiryService.getInquiryStatusCount();
            return ResponseEntity.ok(inquiryStatusCount);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("전체 상태 카운트 조회 실패: 서버 오류 발생");
        }
    }

    @PostMapping("/status/count/my")
    @Operation(summary = "특정 회원의 상태별 문의 카운트", description = "특정 회원의 상태별 문의 카운트를 조회합니다.")
    public ResponseEntity<?> getInquiryStatusCountByMember(@RequestBody String memberId) {
        try {
            InquiryDto.InquiryStatusCountResponse inquiryStatusCount = inquiryService.getInquiryStatusCountByMember(memberId);
            return ResponseEntity.ok(inquiryStatusCount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("회원 조회 실패: 잘못된 회원 ID");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("회원 상태 카운트 조회 실패: 서버 오류 발생");
        }
    }
}
