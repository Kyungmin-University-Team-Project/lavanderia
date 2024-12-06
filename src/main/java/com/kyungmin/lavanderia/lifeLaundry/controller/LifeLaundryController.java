package com.kyungmin.lavanderia.lifeLaundry.controller;

import com.kyungmin.lavanderia.lifeLaundry.data.dto.LifeLaundryDTO;
import com.kyungmin.lavanderia.lifeLaundry.service.LifeLaundryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "생활 빨래 API")
@RequestMapping("/life-laundry")
@RequiredArgsConstructor
@RestController
public class LifeLaundryController {

    private final LifeLaundryService lifeLaundryService;

    @PostMapping("/add")
    @Operation(summary = "생활 빨래 장바구니 추가", description = "생활 빨래를 장바구니에 추가합니다.<br><br> " +
            "Post 요청, access 토큰 필요<br><br>필요 타입<br> " +
            "type(String): 생활 빨래 종류<br> " + "weight(number): 무게<br> " + "price(number): 가격")
    public ResponseEntity<?> addLifeLaundry (@RequestBody LifeLaundryDTO.InsertLifeLaundryDTO insertLifeLaundryDTO) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            lifeLaundryService.addLifeLaundry(insertLifeLaundryDTO, memberId);
            return ResponseEntity.ok().body("생활 빨래 장바구니 추가 성공");
        } catch (Exception e) {
            log.error("/life-laundry/add - {} - {}", e.getMessage(),memberId);
            return ResponseEntity.badRequest().body("생활 빨래 장바구니 추가 실패 : " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @Operation(summary = "생활 빨래 장바구니 삭제", description = "생활 빨래를 장바구니에서 삭제합니다.<br><br> " +
            "Post 요청, access 토큰 필요<br><br>필요 타입<br> " + "repairCartId(number): 생활 빨래 장바구니 ID")
    public ResponseEntity<?> deleteLifeLaundry(@RequestBody LifeLaundryDTO.CartIdDTO cartIdDTO) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            lifeLaundryService.deleteLifeLaundry(cartIdDTO.getLifeLaundryCartId(), memberId);
            return ResponseEntity.ok().body("생활 빨래 장바구니 삭제 성공");
        } catch (Exception e) {
            log.error("/life-laundry/delete - {} - {}", e.getMessage(), memberId);
            return ResponseEntity.badRequest().body("생활 빨래 장바구니 삭제 실패 : " + e.getMessage());
        }
    }

    @PostMapping("/cart-list")
    @Operation(summary = "생활 빨래 카트 리스트 조회", description = "생활 빨래 카트 리스트를 조회합니다.<br><br> " +
            "Post 요청, access 토큰 필요<br><br>반환 값<br> " +
            "lifeLaundryCartId(number): 생활 빨래 장바구니 ID<br> " + "lifeLaundryId(number): 생활 빨래 ID<br> " +
            "memberId(String): 회원 ID<br> " + "type(String): 생활 빨래 종류<br> " + "weight(number): 무게<br> " + "price(number): 가격")
    public ResponseEntity<?> getLifeLaundryList() {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            List<LifeLaundryDTO.LifeLaundryCartResponseDTO> lifeLaundryCartList = lifeLaundryService.getLifeLaundryCartList(memberId);
            return ResponseEntity.ok(lifeLaundryCartList);
        } catch (Exception e) {
            log.error("/life-laundry/cart-list - {} - {}", e.getMessage(), memberId);
            return ResponseEntity.badRequest().body("생활 빨래 장바구니 목록 조회 실패 : " + e.getMessage());
        }
    }
}