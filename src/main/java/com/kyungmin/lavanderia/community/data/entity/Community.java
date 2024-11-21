package com.kyungmin.lavanderia.community.data.entity;

import com.kyungmin.lavanderia.global.entity.TimeLog;
import com.kyungmin.lavanderia.member.data.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_COMMUNITY")
public class Community extends TimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMUNITY_ID")
    private Long communityId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "VIEW_COUNT")
    private Integer viewCount;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "IMAGE")
    private String image;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<CommunityComment> communityComment;

}