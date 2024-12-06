package com.kyungmin.lavanderia.cart.controller;

import com.kyungmin.lavanderia.cart.data.dto.CartDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class CartController {

    @Operation(summary = "장바구니 추가", description = "장바구니에 상품을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장바구니 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
    })
    @PostMapping("/cart/add")
    private void addCart(CartDTO cartDTO, Principal principal) {
        System.out.println(principal.getName());
    }
}
