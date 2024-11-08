package com.kyungmin.lavanderia.Inquiry.data.entity;

import com.kyungmin.lavanderia.member.data.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TBL_INQUIRY_DETAIL")
public class InquiryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DETAIL_ID")
    private Long detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INQUIRY_ID", nullable = false)
    private Inquiry inquiry; // 연결된 문의 (상위 Inquiry)

    @Column(name = "DETAIL_CONTENT", nullable = false, columnDefinition = "TEXT")
    private String detailContent; // 내용

    @Column(name = "DETAIL_DATE", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime detailDate; // 작성 날짜

    @Column(name = "TYPE", nullable = false, length = 10)
    private String type; // "QUESTION" 또는 "ANSWER"로 구분

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member; // 작성자

    // 다대일 관계로 이미지 리스트를 설정
    @OneToMany(mappedBy = "inquiryDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InquiryImage> inquiryImages = new ArrayList<>(); // 이미지 리스트
}

