package com.kyungmin.lavanderia.community.data.dto;

import com.kyungmin.lavanderia.community.data.entity.Community;
import com.kyungmin.lavanderia.community.data.entity.CommunityComment;
import com.kyungmin.lavanderia.member.data.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long commentId;
    private String memberId;
    private String content;
    private LocalDateTime createdDate;

    public CommentDto(CommunityComment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.memberId = comment.getMember().getMemberId();
        this.createdDate = comment.getCreatedAt();
    }

    public static CommunityComment toEntity(Member member, Community community, CommentDto commentDto) {
        return CommunityComment.builder()
                .member(member)
                .community(community)
                .content(commentDto.getContent())
                .build();
    }

}
