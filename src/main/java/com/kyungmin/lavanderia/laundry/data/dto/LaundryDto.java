package com.kyungmin.lavanderia.laundry.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LaundryDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LaundryInsert {

        @Schema(description = "옷 타입", example = "셔츠")
        private String type;

        @Schema(description = "가격", example = "10000")
        private int price;

    }

}
