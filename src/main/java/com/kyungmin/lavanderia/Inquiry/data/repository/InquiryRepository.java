package com.kyungmin.lavanderia.Inquiry.data.repository;

import com.kyungmin.lavanderia.Inquiry.data.entity.Inquiry;
import com.kyungmin.lavanderia.member.data.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // 특정 회원의 최상위 문의 조회
    @Query("SELECT i FROM Inquiry i WHERE i.member = :member")
    Page<Inquiry> findInquiriesByMember(@Param("member") Member member, Pageable pageable);


    // 상태에 따라 페이징 처리된 문의 목록을 조회하는 메서드
    @Query("SELECT i FROM Inquiry i WHERE i.status = :status")
    Page<Inquiry> findByStatus(@Param("status") String status, Pageable pageable);

    // 전체 답변 대기 중인 문의 수
    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.status = 'PENDING'")
    int countPendingInquiries();

    // 전체 답변 완료된 문의 수
    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.status = 'COMPLETED'")
    int countCompletedInquiries();

    // 회원별 답변 대기 중인 문의 수
    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.status = 'PENDING' AND i.member = :member")
    int countPendingInquiriesByMember(@Param("member") Member member);

    // 회원별 답변 완료된 문의 수
    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.status = 'COMPLETED' AND i.member = :member")
    int countCompletedInquiriesByMember(@Param("member") Member member);

}