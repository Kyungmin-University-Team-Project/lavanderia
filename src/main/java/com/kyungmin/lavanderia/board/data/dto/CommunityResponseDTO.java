package com.kyungmin.lavanderia.board.data.dto;

import com.kyungmin.lavanderia.board.data.entity.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public CommunityResponseDTO(Community community) {
        this.communityId = community.getCommunityId();
        this.memberId = community.getMember().getMemberId();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.viewCount = community.getViewCount();
        this.category = community.getCategory();
        this.image = community.getImage();
    }
}
