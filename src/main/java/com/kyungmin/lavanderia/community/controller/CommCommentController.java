package com.kyungmin.lavanderia.community.controller;

import com.kyungmin.lavanderia.community.data.dto.CommentDto;
import com.kyungmin.lavanderia.community.service.CommCommentService;
import com.kyungmin.lavanderia.member.data.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "커뮤니티 댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommCommentController {

    private final CommCommentService commCommentService;

    @GetMapping("/post/{communityId}")
    @Operation(summary = "댓글 조회", description = "커뮤니티 게시글의 댓글을 조회합니다.")
    public ResponseEntity<?> getAllComment(@PathVariable Long communityId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(commCommentService.getAllComment(communityId, pageable));
    }

    @PostMapping("/post/{communityId}/comment")
    @Operation(summary = "댓글 등록", description = "커뮤니티 게시글에 댓글을 등록합니다. (로그인 필요)")
    public ResponseEntity<String> addComment(@AuthenticationPrincipal Member member, @PathVariable Long communityId, @RequestBody CommentDto commentDto) {

        commCommentService.addComment(member, communityId, commentDto);

        return ResponseEntity.ok("댓글이 등록되었습니다.");

    }

    @DeleteMapping("/post/{commentId}")
    @Operation(summary = "댓글 삭제", description = "커뮤니티 게시글의 댓글을 삭제합니다. (로그인 필요)")
    public ResponseEntity<String> deleteComment(@AuthenticationPrincipal Member member, @PathVariable Long commentId) {

        commCommentService.deleteComment(member, commentId);

        return ResponseEntity.ok("댓글이 삭제되었습니다.");

    }

    @PatchMapping("/post/{commentId}")
    @Operation(summary = "댓글 수정", description = "커뮤니티 게시글의 댓글을 수정합니다. (로그인 필요)")
    public ResponseEntity<String> updateComment(@AuthenticationPrincipal Member member, @PathVariable Long commentId, @RequestBody CommentDto commentDto) {

        commCommentService.updateComment(member, commentId, commentDto);

        return ResponseEntity.ok("댓글이 수정되었습니다.");

    }

}
