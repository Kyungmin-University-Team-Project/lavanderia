package com.kyungmin.lavanderia.community.service;

import com.kyungmin.lavanderia.community.data.dto.CommunityDTO;
import com.kyungmin.lavanderia.community.data.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface CommunityService {

    void save(CommunityDTO communityDTO, String memberId) throws IOException;

    Page<Community> findAll(Pageable pageable);

    void delete(Long id, String memberId);

    void update(Long id, CommunityDTO communityDTO, String memberId);

    Page<Community> findByCategory(String category, Pageable pageable);
}
