package com.kyungmin.lavanderia.community.data.entity;

import com.kyungmin.lavanderia.global.entity.TimeLog;
import com.kyungmin.lavanderia.member.data.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_COMMUNITY_COMMENT")
public class CommunityComment extends TimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "COMMUNITY_ID")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "CONTENT")
    private String content;

}
