package com.kyungmin.lavanderia.Inquiry.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class InquiryDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    // 최상위 문의 삽입
    public static class InquiryRequest {
        @NotBlank
        private String inquiryTitle; // 문의 제목
        @NotBlank
        private String inquiryContent; // 문의 내용
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // 문의 상세 조회
    public static class InquiryDetailResponse {
        private Long inquiryId;
        private String inquiryTitle;
        private String inquiryDate;
        private String status;
        private String memberName;

        private List<InquiryDetailDTO> inquiryDetails;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // 문의 상세 조회
    public static class InquiryDetailDTO {
        private Long detailId;
        private String detailContent; // 내용
        private String detailDate; // 작성 날짜
        private String type; // "QUESTION" 또는 "ANSWER"로 구분
        private String memberName; // 작성자

        private List<String> inquiryImages;// 이미지 리스트
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // 최상위 문의 조회
    public static class InquiriesResponseDTO {
        private Long inquiryId;
        private String inquiryTitle;
        private String inquiryDate;
        private String status;
        private String memberName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InquiriesByStatusRequestDTO {
        private String status;  // 상태 (PENDING, COMPLETED, ALL)
        private int page;       // 페이지 번호
        private int size;       // 페이지 당 항목 수
        private String sort;  // 정렬 방향 ("ASC" 또는 "DESC")
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InquiriesByMemberRequestDTO {
        private int page;       // 페이지 번호
        private int size;       // 페이지 당 항목 수
        private String sort;  // 정렬 방향 ("ASC" 또는 "DESC")
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InquiryStatusCountResponse {
        private int pendingCount;
        private int completedCount;
        private int totalCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InquiryDetailRequest {
        private Long inquiryId; // 연결된 문의 (상위 Inquiry)
        private String detailContent; // 내용
        private String type; // "QUESTION" 또는 "ANSWER"로 구분
    }
}
