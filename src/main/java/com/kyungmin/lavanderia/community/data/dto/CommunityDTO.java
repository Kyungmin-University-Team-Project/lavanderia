package com.kyungmin.lavanderia.community.data.dto;

import com.kyungmin.lavanderia.community.data.entity.Community;
import com.kyungmin.lavanderia.member.data.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {
    private String title;
    private String content;
    private String category;
    private MultipartFile image;

    public Community toEntity(Member member, String image) {
        return Community.builder().
                title(this.title)
                .content(this.content)
                .category(this.category)
                .viewCount(0)
                .image(image)
                .member(member).build();
    }
}
