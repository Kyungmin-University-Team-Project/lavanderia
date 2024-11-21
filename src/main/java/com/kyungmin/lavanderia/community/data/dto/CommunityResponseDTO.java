package com.kyungmin.lavanderia.community.data.dto;

import com.kyungmin.lavanderia.community.data.entity.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityResponseDTO {
    private Long communityId;
    private String memberId;
    private String title;
    private String content;
    private Integer viewCount;
    private String category;
    private String image;
    private List<CommentDto> comments;

    public CommunityResponseDTO(Community community) {
        this.communityId = community.getCommunityId();
        this.memberId = community.getMember().getMemberId();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.viewCount = community.getViewCount();
        this.category = community.getCategory();
        this.image = community.getImage();
        this.comments = community.getCommunityComment().stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }
}
