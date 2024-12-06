package com.kyungmin.lavanderia.cart.controller;

import com.kyungmin.lavanderia.cart.data.dto.LaundryCartDto;
import com.kyungmin.lavanderia.cart.service.impl.LaundryCartService;
import com.kyungmin.lavanderia.member.data.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "세탁물 카트 API")
@RestController
@RequestMapping("/laundry/cart")
@RequiredArgsConstructor
public class LaundryCartController {

    private final LaundryCartService laundryCartService;

    @GetMapping("/")
    @Operation(summary = "세탁물 카트 조회", description = "세탁물 카트를 조회합니다. (로그인 필요)")
    public ResponseEntity<List<LaundryCartDto>> getAllCart(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(laundryCartService.getAllCart(member));
    }

    @DeleteMapping("/{cartId}")
    @Operation(summary = "세탁물 카트 삭제", description = "세탁물 카트를 삭제합니다. (로그인 필요)")
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal Member member, @PathVariable Long cartId) {
        laundryCartService.deleteCart(member, cartId);
        return ResponseEntity.ok("카트 삭제 성공");
    }
}
