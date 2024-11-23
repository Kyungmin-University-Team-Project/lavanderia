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
    private String profileImg;
    private String content;
    private LocalDateTime createdDate;

    public static CommunityComment toEntity(Member member, Community community, CommentDto commentDto) {
        return CommunityComment.builder()
                .member(member)
                .community(community)
                .content(commentDto.getContent())
                .build();
    }

    public static CommentDto toEntity(CommunityComment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .memberId(comment.getMember().getMemberId())
                .profileImg(comment.getMember().getMemberProfileImg())
                .content(comment.getContent())
                .createdDate(comment.getCreatedAt())
                .build();
    }

}
