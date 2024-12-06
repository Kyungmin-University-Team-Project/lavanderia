package com.kyungmin.lavanderia.order.data.constant;

public enum DeliveryStatus {

    READY("배송 준비중"),
    DELIVERING("배송 중"),
    DELIVERED("배송 완료");

    private String status;

    DeliveryStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
