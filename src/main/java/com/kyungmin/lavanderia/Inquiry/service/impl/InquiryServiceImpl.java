package com.kyungmin.lavanderia.Inquiry.service.impl;

import com.kyungmin.lavanderia.Inquiry.data.dto.InquiryDto;
import com.kyungmin.lavanderia.Inquiry.data.entity.Inquiry;
import com.kyungmin.lavanderia.Inquiry.data.entity.InquiryDetail;
import com.kyungmin.lavanderia.Inquiry.data.entity.InquiryImage;
import com.kyungmin.lavanderia.Inquiry.data.repository.InquiryDetailRepository;
import com.kyungmin.lavanderia.Inquiry.data.repository.InquiryImageRepository;
import com.kyungmin.lavanderia.Inquiry.data.repository.InquiryRepository;
import com.kyungmin.lavanderia.Inquiry.service.InquiryService;
import com.kyungmin.lavanderia.global.util.GoogleCloudUtils;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryDetailRepository inquiryDetailRepository;
    private final InquiryImageRepository inquiryImageRepository;
    private final MemberRepository memberRepository;

    // 새 최상위 문의를 삽입하는 메서드
    @Override
    public void insertInquiry(Member member, List<MultipartFile> inquiryImageList, InquiryDto.InquiryRequest request) {
        try {
            // Inquiry 엔티티 생성 및 저장
            Inquiry inquiry = Inquiry.builder()
                    .member(member)
                    .inquiryTitle(request.getInquiryTitle())
                    .status("PENDING")
                    .build();
            inquiryRepository.save(inquiry);

            // 첫 번째 InquiryDetail 생성 (최상위 문의의 내용)
            InquiryDetail inquiryDetail = InquiryDetail.builder()
                    .inquiry(inquiry)
                    .detailContent(request.getInquiryContent())
                    .type("QUESTION")
                    .member(member)
                    .build();
            inquiryDetailRepository.save(inquiryDetail);

            // 이미지가 존재하면 이미지 저장
            if (inquiryImageList != null && !inquiryImageList.isEmpty()) {
                saveInquiryImages(inquiryImageList, inquiryDetail);
            }
        } catch (Exception e) {
            throw new IllegalStateException("문의 등록 중 문제가 발생했습니다.", e);
        }
    }

    // 추가 문의 또는 답변을 삽입하는 메서드
    @Override
    public void insertInquiryDetail(Member member, List<MultipartFile> imageList, InquiryDto.InquiryDetailRequest request) {
        // 상위 문의를 조회
        Inquiry parentInquiry = inquiryRepository.findById(request.getInquiryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 문의를 찾을 수 없습니다."));

        try {
            // 답변일 경우, 관리자 권한을 확인하고 상태를 변경
            if (request.getType().equals("ANSWER")) {
                if (!isRoleAdmin(member)) {
                    throw new SecurityException("관리자 권한이 없습니다.");
                }
                parentInquiry.setStatus("COMPLETED");
            } else {
                parentInquiry.setStatus("PENDING");
            }

            // InquiryDetail 엔티티 생성 및 저장
            InquiryDetail inquiryDetail = InquiryDetail.builder()
                    .inquiry(parentInquiry)
                    .detailContent(request.getDetailContent())
                    .type(request.getType())
                    .member(member)
                    .build();
            inquiryDetailRepository.save(inquiryDetail);
            inquiryRepository.save(parentInquiry);

            // 이미지가 존재하면 이미지 저장
            if (imageList != null && !imageList.isEmpty()) {
                saveInquiryImages(imageList, inquiryDetail);
            }
        } catch (Exception e) {
            throw new IllegalStateException("문의 디테일 추가 중 문제가 발생했습니다.", e);
        }
    }

    // InquiryDetail에 이미지를 저장하는 메서드
    private void saveInquiryImages(List<MultipartFile> inquiryImageList, InquiryDetail inquiryDetail) {
        try {
            // Google Cloud에 파일을 업로드하고 URL 리스트를 받아옴
            List<String> urlList = GoogleCloudUtils.uploadListFile(inquiryImageList);
            List<InquiryImage> inquiryImages = new ArrayList<>();
            for (String url : urlList) {
                inquiryImages.add(InquiryImage.builder()
                        .inquiryDetail(inquiryDetail)
                        .imagePath(url)
                        .build());
            }
            inquiryImageRepository.saveAll(inquiryImages); // 이미지 리스트를 한 번에 저장
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
        }
    }

    // 관리자인지 확인하는 메서드
    private boolean isRoleAdmin(Member member) {
        return member.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthorities()));
    }

    // 특정 문의에 대한 상세 내용을 조회하는 메서드
    @Override
    public InquiryDto.InquiryDetailResponse getInquiryDetail(Member member, Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의를 찾을 수 없습니다."));

        // 권한이 있는지 확인
        if (!inquiry.getMember().getMemberId().equals(member.getMemberId()) && !isRoleAdmin(member)) {
            throw new SecurityException("해당 문의를 조회할 권한이 없습니다.");
        }

        List<InquiryDetail> inquiryDetailList = inquiry.getInquiryDetails();

        // InquiryDetail 리스트를 InquiryDetailResponse DTO로 변환하여 반환
        return InquiryDto.InquiryDetailResponse.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryDate(String.valueOf(inquiry.getInquiryDate()))
                .status(inquiry.getStatus())
                .memberName(inquiry.getMember().getMemberName())
                .inquiryDetails(convertToInquiryDetailDTO(inquiryDetailList))
                .build();
    }

    // InquiryDetail을 InquiryDetailDTO로 변환
    private List<InquiryDto.InquiryDetailDTO> convertToInquiryDetailDTO(List<InquiryDetail> inquiryDetailList) {
        List<InquiryDto.InquiryDetailDTO> responseList = new ArrayList<>();
        for (InquiryDetail inquiryDetail : inquiryDetailList) {
            responseList.add(InquiryDto.InquiryDetailDTO.builder()
                    .detailId(inquiryDetail.getDetailId())
                    .detailContent(inquiryDetail.getDetailContent())
                    .detailDate(String.valueOf(inquiryDetail.getDetailDate()))
                    .type(inquiryDetail.getType())
                    .memberName(inquiryDetail.getMember().getMemberName())
                    .inquiryImages(inquiryDetail.getInquiryImages().stream()
                            .map(InquiryImage::getImagePath)
                            .toList())
                    .build());
        }
        return responseList;
    }

    // 특정 회원의 최상위 문의를 조회하는 메서드
    @Override
    public List<InquiryDto.InquiriesResponseDTO> getInquiriesByMember(Member member, InquiryDto.InquiriesByMemberRequestDTO request) {
        Sort.Direction direction = request.getSort().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(direction, "inquiryDate"));

        Page<Inquiry> inquiries = inquiryRepository.findInquiriesByMember(member, pageable);

        List<InquiryDto.InquiriesResponseDTO> responseList = new ArrayList<>();
        inquiries.forEach(inquiry -> responseList.add(convertToInquiriesResponseDTO(inquiry)));
        return responseList;
    }

    // Inquiry -> InquiriesResponseDTO로 변환하는 메서드
    private InquiryDto.InquiriesResponseDTO convertToInquiriesResponseDTO(Inquiry inquiry) {
        return InquiryDto.InquiriesResponseDTO.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryDate(String.valueOf(inquiry.getInquiryDate()))
                .status(inquiry.getStatus())
                .memberName(inquiry.getMember().getMemberName())
                .build();
    }

    // 문의를 삭제하는 메서드
    @Override
    public void deleteInquiry(Long inquiryId, Member member) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의를 찾을 수 없습니다."));

        if (!inquiry.getMember().getMemberId().equals(member.getMemberId()) && !isRoleAdmin(member)) {
            throw new SecurityException("해당 문의를 삭제할 권한이 없습니다.");
        }

        inquiryRepository.delete(inquiry);
    }

    // 상태에 따른 문의 목록을 조회하는 메서드
    @Override
    public List<InquiryDto.InquiriesResponseDTO> getInquiriesByStatus(InquiryDto.InquiriesByStatusRequestDTO request) {
        Sort.Direction direction = request.getSort().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(direction, "inquiryDate"));

        Page<Inquiry> inquiryPage = request.getStatus().equalsIgnoreCase("ALL")
                ? inquiryRepository.findAll(pageable)
                : inquiryRepository.findByStatus(request.getStatus(), pageable);

        return inquiryPage.stream()
                .map(this::convertToInquiriesResponseDTO)
                .toList();
    }

    // 전체 문의 상태 카운트를 조회하는 메서드
    @Override
    public InquiryDto.InquiryStatusCountResponse getInquiryStatusCount() {
        int pendingCount = inquiryRepository.countPendingInquiries();
        int completedCount = inquiryRepository.countCompletedInquiries();
        return InquiryDto.InquiryStatusCountResponse.builder()
                .pendingCount(pendingCount)
                .completedCount(completedCount)
                .totalCount(pendingCount + completedCount)
                .build();
    }

    // 특정 회원의 문의 상태 카운트를 조회하는 메서드
    @Override
    public InquiryDto.InquiryStatusCountResponse getInquiryStatusCountByMember(String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        int pendingCount = inquiryRepository.countPendingInquiriesByMember(member);
        int completedCount = inquiryRepository.countCompletedInquiriesByMember(member);
        return InquiryDto.InquiryStatusCountResponse.builder()
                .pendingCount(pendingCount)
                .completedCount(completedCount)
                .totalCount(pendingCount + completedCount)
                .build();
    }
}
