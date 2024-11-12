package com.kyungmin.lavanderia.order.controller;

import com.kyungmin.lavanderia.global.ResponseEntityUtil;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.order.data.dto.OrderDTO.OrderInsert;
import com.kyungmin.lavanderia.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "주문 API")
@RequestMapping("/order")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add")
    @ApiResponse(responseCode = "200", description = "주문 ID 반환 성공")
    @ApiResponse(responseCode = "400", description = "주문 ID 반환 실패")
    @Operation(summary = "주문 추가", description = "주문 정보를 추가하고 주문 ID를 반환합니다.")
    public ResponseEntity<Long> addOrder(@RequestBody OrderInsert orderDTO, @AuthenticationPrincipal Member member) {

        Long orderId = orderService.insertOrder(member, orderDTO);

        return ResponseEntityUtil.response(orderId, HttpStatus.OK);
    }

}