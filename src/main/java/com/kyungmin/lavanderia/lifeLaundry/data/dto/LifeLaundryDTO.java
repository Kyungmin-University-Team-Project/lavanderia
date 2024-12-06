package com.kyungmin.lavanderia.lifeLaundry.data.dto;

import lombok.*;

public class LifeLaundryDTO {


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InsertLifeLaundryDTO {
        private String type; // 상품 타입
        private double weight; // 상품 무게
        private int price; // 상품 가격
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartIdDTO {
        private Long lifeLaundryCartId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LifeLaundryCartResponseDTO {
        private Long lifeLaundryCartId;
        private Long lifeLaundryId;
        private String memberId;
        private String type;
        private double weight;
        private int price;
    }
}