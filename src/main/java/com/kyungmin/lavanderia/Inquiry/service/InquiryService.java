package com.kyungmin.lavanderia.Inquiry.service;

import com.kyungmin.lavanderia.Inquiry.data.dto.InquiryDto;
import com.kyungmin.lavanderia.member.data.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InquiryService {

    // 문의 삽입
    void insertInquiry(Member member, List<MultipartFile> inquiryImageList, InquiryDto.InquiryRequest inquiryRequest);

    // 추가 문의, 답변 삽입
    void insertInquiryDetail(Member member, List<MultipartFile> imageList ,InquiryDto.InquiryDetailRequest inquiryDetailRequest);

    // 특정 문의와 그에 대한 모든 답변 및 추가 문의 조회
    InquiryDto.InquiryDetailResponse getInquiryDetail(Member member, Long inquiryId);

    // 특정 회원의 최상위 문의 조회
    List<InquiryDto.InquiriesResponseDTO> getInquiriesByMember(Member member, InquiryDto.InquiriesByMemberRequestDTO request);

    // 문의 삭제
    void deleteInquiry(Long inquiryId, Member member);

    // ------------------- 관리자 기능 -------------------


    // 상태에 따라 문의 목록을 조회하는 API
    List<InquiryDto.InquiriesResponseDTO> getInquiriesByStatus(InquiryDto.InquiriesByStatusRequestDTO request);

    // 전체 회원 문의 status count 조회
    InquiryDto.InquiryStatusCountResponse getInquiryStatusCount();

    // 회원 별 문의 status count 조회
    InquiryDto.InquiryStatusCountResponse getInquiryStatusCountByMember(String memberId);
}
