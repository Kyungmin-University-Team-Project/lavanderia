package com.kyungmin.lavanderia.community.service;

import com.kyungmin.lavanderia.community.data.dto.CommentDto;
import com.kyungmin.lavanderia.member.data.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommCommentService {

    void addComment(Member member, Long postId, CommentDto commentDto);

    void deleteComment(Member member, Long commentId);

    void updateComment(Member member, Long commentId, CommentDto commentDto);

    Page<CommentDto> getAllComment(Long communityId, Pageable pageable);

}
