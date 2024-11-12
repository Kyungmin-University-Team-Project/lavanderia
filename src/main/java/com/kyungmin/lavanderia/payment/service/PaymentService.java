package com.kyungmin.lavanderia.payment.service;

import com.kyungmin.lavanderia.payment.data.dto.PaymentDTO.PaymentInfo;

public interface PaymentService {
    PaymentInfo getPaymentDetails(String paymentId);

    void processPayment(PaymentInfo payment, Long orderId);
}
