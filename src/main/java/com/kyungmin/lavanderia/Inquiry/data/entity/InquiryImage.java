package com.kyungmin.lavanderia.Inquiry.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TBL_INQUIRY_IMAGE")
public class InquiryImage {

    @Id
    @Column(name = "INQUIRY_IMAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryImageId;

    @Column(name = "IMAGE_PATH", nullable = false, length = 500)
    private String imagePath; // 이미지 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DETAIL_ID")  // 외래 키로 설정
    private InquiryDetail inquiryDetail; // 참조된 문의 상세
}

