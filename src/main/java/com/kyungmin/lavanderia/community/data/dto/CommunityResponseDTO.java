package com.kyungmin.lavanderia.community.data.dto;

import com.kyungmin.lavanderia.community.data.entity.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityResponseDTO {
    private Long communityId;
    private String memberId;
    private LocalDateTime createdDate;
    private String title;
    private String content;
    private Integer viewCount;
    private String category;
    private String image;
    private String profileImg;

    public CommunityResponseDTO(Community community) {
        this.communityId = community.getCommunityId();
        this.memberId = community.getMember().getMemberId();
        this.createdDate = community.getCreatedAt();
        this.title = community.getTitle();
        this.content = community.getContent();
        this.viewCount = community.getViewCount();
        this.category = community.getCategory();
        this.image = community.getImage();
        this .profileImg = community.getMember().getMemberProfileImg();
    }
}
