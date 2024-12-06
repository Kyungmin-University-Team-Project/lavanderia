package com.kyungmin.lavanderia.payment.controller;

import com.kyungmin.lavanderia.cart.service.impl.LaundryCartService;
import com.kyungmin.lavanderia.laundry.service.LaundryService;
import com.kyungmin.lavanderia.lifeLaundry.service.LifeLaundryService;
import com.kyungmin.lavanderia.order.data.entity.Order;
import com.kyungmin.lavanderia.order.data.entity.OrderDetail;
import com.kyungmin.lavanderia.order.data.repository.OrderRepository;
import com.kyungmin.lavanderia.payment.data.dto.PaymentDTO;
import com.kyungmin.lavanderia.payment.data.dto.PaymentDTO.PaymentInfo;
import com.kyungmin.lavanderia.payment.service.PaymentService;
import com.kyungmin.lavanderia.repair.service.RepairService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Tag(name = "결제 API")
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final RepairService repairService;
    private final LifeLaundryService lifeLaundryService;
    private final LaundryCartService laundryCartService;

    private final OrderRepository orderRepository;

    @Value("${flask.server.url}")
    private String flaskServerUrl;

    @PostMapping("/complete")
    public ResponseEntity<String> completePayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            // 요청의 body에서 paymentId와 order를 받습니다.
            String paymentId = paymentDTO.getPaymentId();
            Long orderId = paymentDTO.getOrderId();

            // 1. 포트원 결제내역 단건조회
            PaymentInfo paymentInfo = paymentService.getPaymentDetails(paymentId);

            // 2. 고객사 내부 주문 데이터의 가격과 실제 지불된 금액을 비교
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found."));

            // 주문 상세 정보의 가격을 합산
            //int totalPrice = order.getOrderDetailList().stream().mapToInt(OrderDetail::getPrice).sum();
            int totalPrice = order.getTotalPrice();

            if (totalPrice == paymentInfo.getAmount().getTotal()) {
                // 3. 결제 상태에 따라 처리
                paymentService.processPayment(paymentInfo, orderId);

                if (order.getRepairList() != null) {
                    repairService.deleteRepairCart(order.getRepairList());
                }

                if (order.getLifeLaundryList() != null) {
                    lifeLaundryService.deleteLifeLaundryCart(order.getLifeLaundryList());
                }

                if(order.getLaundryList() != null) {
                    laundryCartService.deleteAllCart(order.getLaundryList());
                }

            } else {
                // 결제 금액이 불일치하여 위/변조 시도가 의심됩니다.
                return ResponseEntity.status(400).body("Payment amount mismatch.");
            }

            return ResponseEntity.ok("Payment completed successfully.");

        } catch (Exception e) {
            // 결제 검증에 실패했습니다.
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}