package com.kyungmin.lavanderia.Inquiry.data.repository;

import com.kyungmin.lavanderia.Inquiry.data.entity.InquiryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryImageRepository extends JpaRepository<InquiryImage, Long> {
}