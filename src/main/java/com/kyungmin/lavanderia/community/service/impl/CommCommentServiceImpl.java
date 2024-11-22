package com.kyungmin.lavanderia.community.service.impl;

import com.kyungmin.lavanderia.community.data.dto.CommentDto;
import com.kyungmin.lavanderia.community.data.entity.Community;
import com.kyungmin.lavanderia.community.data.entity.CommunityComment;
import com.kyungmin.lavanderia.community.repository.CommCommentRepository;
import com.kyungmin.lavanderia.community.repository.CommunityRepository;
import com.kyungmin.lavanderia.community.service.CommCommentService;
import com.kyungmin.lavanderia.member.data.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommCommentServiceImpl implements CommCommentService {

    private final CommCommentRepository commCommentRepository;

    private final CommunityRepository communityRepository;

    public Page<CommentDto> getAllComment(Long communityId, Pageable pageable) {
        return commCommentRepository.findByCommunityCommunityId(communityId, pageable)
                .map(CommentDto::toEntity);
    }

    @Override
    public void addComment(Member member, Long communityId, CommentDto commentDto) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        CommunityComment communityComment = CommentDto.toEntity(member, community, commentDto);

        commCommentRepository.save(communityComment);

    }

    @Override
    public void deleteComment(Member member, Long commentId) {

            CommunityComment communityComment = commCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

            if (!communityComment.getMember().getMemberId().equals(member.getMemberId())) {
                throw new IllegalArgumentException("본인의 댓글만 삭제할 수 있습니다.");
            }

            commCommentRepository.delete(communityComment);
    }

    @Override
    public void updateComment(Member member, Long commentId, CommentDto commentDto) {

            CommunityComment communityComment = commCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

            if (!communityComment.getMember().getMemberId().equals(member.getMemberId())) {
                throw new IllegalArgumentException("본인의 댓글만 수정할 수 있습니다.");
            }

            commCommentRepository.save(communityComment);
    }
}
