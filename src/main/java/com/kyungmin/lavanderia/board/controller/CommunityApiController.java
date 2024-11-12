package com.kyungmin.lavanderia.board.controller;

import com.kyungmin.lavanderia.board.data.dto.CommunityDTO;
import com.kyungmin.lavanderia.board.data.dto.CommunityResponseDTO;
import com.kyungmin.lavanderia.board.data.entity.Community;
import com.kyungmin.lavanderia.board.service.CommunityService;
import com.kyungmin.lavanderia.member.data.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityApiController {

    private final CommunityService communityService;

    @PostMapping("/save")
    public ResponseEntity<String> save(CommunityDTO communityDTO, @AuthenticationPrincipal Member member) throws IOException {
        communityService.save(communityDTO, member.getMemberId());
        return response(HttpStatus.OK, "save");
    }

    @GetMapping("/")
    public ResponseEntity<?> findAll() {
        List<Community> communities = communityService.findAll();
        return ResponseEntity.ok(communities.stream().map(CommunityResponseDTO::new).collect(Collectors.toList()));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal Member member) {
        communityService.delete(id, member.getMemberId());
        return response(HttpStatus.OK, "delete");
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, CommunityDTO communityDTO, @AuthenticationPrincipal Member member) {
        communityService.update(id, communityDTO, member.getMemberId());
        return response(HttpStatus.OK, "update");
    }

    public ResponseEntity<String> response(HttpStatus httpStatus, String result) {
        return ResponseEntity.status(httpStatus)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
                .body(result);
    }

}