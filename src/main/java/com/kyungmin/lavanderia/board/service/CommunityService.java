package com.kyungmin.lavanderia.board.service;

import com.kyungmin.lavanderia.board.data.dto.CommunityDTO;
import com.kyungmin.lavanderia.board.data.entity.Community;

import java.io.IOException;
import java.util.List;

public interface CommunityService {

    void save(CommunityDTO communityDTO,  String memberId) throws IOException;

    List<Community> findAll();

    void delete(Long id, String memberId);

    void update(Long id, CommunityDTO communityDTO, String memberId);
}
