package com.kyungmin.lavanderia.community.controller;

import com.kyungmin.lavanderia.community.data.dto.CommunityDTO;
import com.kyungmin.lavanderia.community.data.dto.CommunityResponseDTO;
import com.kyungmin.lavanderia.community.data.entity.Community;
import com.kyungmin.lavanderia.community.service.CommunityService;
import com.kyungmin.lavanderia.member.data.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public ResponseEntity<?> findAll(@PageableDefault(size = 10, sort = "communityId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Community> communities = communityService.findAll(pageable);
        Page<CommunityResponseDTO> response = communities.map(CommunityResponseDTO::new);
        return ResponseEntity.ok(response);
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

    @GetMapping("/category")
    public ResponseEntity<?> findByCategory(@RequestParam String category, @PageableDefault(size = 10, sort = "communityId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Community> communities = communityService.findByCategory(category, pageable);
        Page<CommunityResponseDTO> response = communities.map(CommunityResponseDTO::new);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> response(HttpStatus httpStatus, String result) {
        return ResponseEntity.status(httpStatus)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
                .body(result);
    }

}