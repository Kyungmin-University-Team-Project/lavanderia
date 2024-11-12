package com.kyungmin.lavanderia.order.data.dto;

import com.kyungmin.lavanderia.order.data.dto.OrderDetailDTO.OrderDetailInsert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class OrderDTO {

    private OrderDTO() {
        throw new IllegalStateException("Utility class");
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderInsert {

        @Schema(description = "수령인 이름", example = "홍길동")
        private String rcvrName;

        @Schema(description = "수령인 전화번호", example = "010-1234-5678")
        private String rcvrPhone;

        @Schema(description = "수령인 주소", example = "서울특별시 강남구 테헤란로 123")
        private String rcvrAddress;

        @Schema(description = "수령인 상세 주소", example = "105-1505")
        private String rcvrDetailAddress;

        @Schema(description = "수령인 우편번호", example = "12345")
        private String rcvrPostalCode;

        @Schema(description = "배송 요청 메시지", example = "문 앞에 두고 가세요")
        private String dlvrReqMessage;

        @Schema(description = "주문 상세 정보")
        private List<OrderDetailInsert> orderDetailList;

    }

}