package com.kyungmin.lavanderia.laundry.controller;


import com.kyungmin.lavanderia.laundry.data.dto.LaundryDto.LaundryInsert;
import com.kyungmin.lavanderia.laundry.service.LaundryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "세탁물 API")
@RestController
@RequestMapping("/laundry")
@RequiredArgsConstructor
public class LaundryController {

    private static final Logger log = LogManager.getLogger(LaundryController.class);
    private final LaundryService laundryService;

    // 세탁물 등록
    @PostMapping("/add")
    @ApiResponse(responseCode = "200", description = "세탁물 등록 성공")
    @Operation(summary = "세탁물 등록", description = "세탁물 정보를 등록합니다.")
    public ResponseEntity<String> addLaundry(@RequestPart List<LaundryInsert> laundryInserts, @RequestPart List<MultipartFile> laundryImages) throws IOException {

        laundryService.addLaundra(laundryInserts, laundryImages);

        return ResponseEntity.ok("세탁물 등록 성공");
    }

    // 세탁물 조회
    /*@GetMapping("/{laundryId}")
    public ResponseEntity<LaundryDTO.LaundryResponse> findLaundry(@PathVariable Long laundryId) {
        return ResponseEntity.ok(laundryService.findLaundry(laundryId));
    }

    // 세탁물 수정
    @PutMapping("/{laundryId}")
    public ResponseEntity<LaundryDTO.LaundryResponse> updateLaundry(@PathVariable Long laundryId, @RequestBody LaundryDTO.LaundryUpdate laundryUpdate) {
        return ResponseEntity.ok(laundryService.updateLaundry(laundryId, laundryUpdate));
    }

    // 세탁물 삭제
    @DeleteMapping("/{laundryId}")
    public ResponseEntity<Void> deleteLaundry(@PathVariable Long laundryId) {
        laundryService.deleteLaundry(laundryId);
        return ResponseEntity.noContent().build();
    }

    // 세탁물 전체 조회
    @GetMapping
    public ResponseEntity<List<LaundryDTO.LaundryResponse>> findAllLaundry() {
        return ResponseEntity.ok(laundryService.findAllLaundry());
    }

    // 세탁물 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<List<LaundryDTO.LaundryDetailResponse>> findAllLaundryDetail() {
        return ResponseEntity.ok(laundryService.findAllLaundryDetail());
    }

    // 세탁물 상세 조회
    @GetMapping("/detail/{laundryId}")
    public ResponseEntity<LaundryDTO.LaundryDetailResponse> findLaundryDetail(@PathVariable Long laundryId) {
        return ResponseEntity.ok(laundryService.findLaundryDetail(laundryId));
    }

    // 세탁물 상세 조회
    @GetMapping("/detail/{laundryId}/order")
    public ResponseEntity<LaundryDTO.LaundryDetailResponse> findLaundryDetailOrder(@PathVariable Long laundryId) {
        return ResponseEntity.ok(laundryService.findLaundryDetailOrder(laundryId));
    }*/

}
