package com.kyungmin.lavanderia.laundry.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyungmin.lavanderia.laundry.data.dto.LaundryDto.LaundryInsert;
import com.kyungmin.lavanderia.laundry.service.LaundryService;
import com.kyungmin.lavanderia.member.data.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Tag(name = "세탁물 카트 API")
@RestController
@RequestMapping("/laundry")
@RequiredArgsConstructor
public class LaundryController {

    @Value("${flask.server.url}")
    private String flaskServerUrl;

    private static final Logger log = LogManager.getLogger(LaundryController.class);
    private final LaundryService laundryService;

    // 세탁물 등록
    @PostMapping("/add")
    @ApiResponse(responseCode = "200", description = "세탁물 등록 성공")
    @Operation(summary = "세탁물 등록", description = "세탁물 정보를 등록합니다. (로그인 필요) 세탁물 등록시 자동으로 세탁물 카트에 추가됩니다.")
    public ResponseEntity<String> addLaundry(@AuthenticationPrincipal Member member, @RequestPart LaundryInsert laundryInsert, @RequestPart MultipartFile laundryImages) throws IOException {

        laundryService.addLaundra(member, laundryInsert, laundryImages);

        return ResponseEntity.ok("세탁물 등록 성공");
    }

    /*@GetMapping
    public ResponseEntity<List<LaundryDTO.LaundryResponse>> findAllLaundry() {
        return ResponseEntity.ok(laundryService.findAllLaundry());
    }*/

    // 가격 책정
    @PostMapping("/price")
    @Operation(summary = "가격 책정", description = "이미지를 전송하여 가격을 책정합니다.")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Flask 서버 URL을 환경 변수에서 가져옴
            String flaskUrl = flaskServerUrl + "/process-image";

            // 이미지 파일 전송 준비
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // MultiValueMap에 파일 추가
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(convertToFile(file)));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Flask 서버로 요청 전송
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, String.class);

            // Flask 서버에서 반환된 결과값 처리
            // 결과값 예시
            /*
            {
              "class_name": "Dress",
              "confidence": 0.8953929543495178,
              "suggested_price": 45000
            }
             */

            // JSON 문자열 파싱하여 필요한 값 추출
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});


            // 값 추출
            String className = (String) responseMap.get("class_name");
            int suggestedPrice = (int) responseMap.get("suggested_price");

            // LaundryInsert 객체 생성
            LaundryInsert laundryInsert = LaundryInsert.builder()
                    .type(className)
                    .price(suggestedPrice)
                    .build();

            return ResponseEntity.ok().body(laundryInsert);

        } catch (Exception e) {
            System.err.println("Error processing image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image: " + e.getMessage());
        }
    }

    private File convertToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
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
