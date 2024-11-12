package com.kyungmin.lavanderia.Inquiry.data.entity;

import com.kyungmin.lavanderia.member.data.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TBL_INQUIRY")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INQUIRY_ID")
    private Long inquiryId;

    @Column(name = "INQUIRY_TITLE", nullable = false, length = 100)
    private String inquiryTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member; // 작성자

    @Column(name = "INQUIRY_DATE", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime inquiryDate; // 작성일

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status = "PENDING"; // 상태 (예: PENDING, COMPLETED)

    // Inquiry와 InquiryDetail의 관계 설정 (OneToMany로 연결)
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("detailDate ASC")
    private List<InquiryDetail> inquiryDetails = new ArrayList<>(); // 문의 상세 리스트
}



