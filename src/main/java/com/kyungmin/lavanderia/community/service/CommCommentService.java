package com.kyungmin.lavanderia.community.service;

import com.kyungmin.lavanderia.community.data.dto.CommentDto;
import com.kyungmin.lavanderia.member.data.entity.Member;

public interface CommCommentService {

    void addComment(Member member, Long postId, CommentDto commentDto);

    void deleteComment(Member member, Long commentId);

    void updateComment(Member member, Long commentId, CommentDto commentDto);
}
