package com.kyungmin.lavanderia.Inquiry.data.repository;

import com.kyungmin.lavanderia.Inquiry.data.entity.InquiryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryDetailRepository extends JpaRepository<InquiryDetail, Long> {

}