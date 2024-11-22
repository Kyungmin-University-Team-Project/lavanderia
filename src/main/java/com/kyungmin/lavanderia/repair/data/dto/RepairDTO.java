package com.kyungmin.lavanderia.repair.data.dto;

import lombok.*;

public class RepairDTO {


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InsertRepairDTO {
        private String clothesType; // 옷 종류
        private String howTo; // 어떻게
        private String detailInfo; // 상세 정보
        private String request; // 요청 사항
        private int price; // 가격
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartIdDTO {
        private Long repairCartId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RepairCartResponseDTO {
        private Long repairCartId;
        private Long repairId;
        private String memberId;
        private String clothesType;
        private String howTo;
        private String detailInfo;
        private String request;
        private int price;
    }
}