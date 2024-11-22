package com.kyungmin.lavanderia.payment.controller;

import com.kyungmin.lavanderia.order.data.entity.Order;
import com.kyungmin.lavanderia.order.data.entity.OrderDetail;
import com.kyungmin.lavanderia.order.data.repository.OrderRepository;
import com.kyungmin.lavanderia.payment.data.dto.PaymentDTO;
import com.kyungmin.lavanderia.payment.data.dto.PaymentDTO.PaymentInfo;
import com.kyungmin.lavanderia.payment.service.PaymentService;
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
            int totalPrice = order.getOrderDetailList().stream().mapToInt(OrderDetail::getPrice).sum();

            if (totalPrice == paymentInfo.getAmount().getTotal()) {
                // 3. 결제 상태에 따라 처리
                paymentService.processPayment(paymentInfo, orderId);
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

    // 가격 책정
    @PostMapping("/price")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Flask 서버 URL을 환경 변수에서 가져옴
            String flaskUrl = flaskServerUrl + "/process-image";

            // 이미지 파일 전송 준비
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // MultiValueMap에 파일 추가
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(convertToFile(file)));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Flask 서버로 요청 전송
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, String.class);

            // Flask 서버에서 반환된 결과값 처리
            // 결과값 예시
            /*
            {
              "class_name": "Dress",
              "confidence": 0.8953929543495178,
              "suggested_price": 45000
            }
             */
            System.out.println("Response from Flask server: " + response.getBody());
            return ResponseEntity.ok("결과값 : " + response.getBody());

        } catch (Exception e) {
            System.err.println("Error processing image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image: " + e.getMessage());
        }
    }

    private File convertToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}