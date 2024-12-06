package com.kyungmin.lavanderia.payment.service.impl;

import com.kyungmin.lavanderia.order.data.repository.OrderRepository;
import com.kyungmin.lavanderia.payment.data.dto.PaymentDTO.PaymentInfo;
import com.kyungmin.lavanderia.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final OrderRepository orderRepository;

    @Value("${spring.portone.apiSecret}")
    private String protoneSecret;

    @Override
    public PaymentInfo getPaymentDetails(String paymentId) {
        // 포트원 결제내역 단건조회 API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + protoneSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<PaymentInfo> paymentResponse = restTemplate.exchange(
                "https://api.portone.io/payments/" + paymentId,
                HttpMethod.GET,
                entity,
                PaymentInfo.class
        );

        if (!paymentResponse.getStatusCode().is2xxSuccessful()) {
            log.info("paymentResponse: {}", paymentResponse.getBody());
            throw new RuntimeException("paymentResponse: " + paymentResponse.getBody());
        }

        return paymentResponse.getBody();
    }

    @Override
    public void processPayment(PaymentInfo paymentInfo, Long orderId) {

        switch (paymentInfo.getStatus()) {
            case "VIRTUAL_ACCOUNT_ISSUED": {
                //String paymentMethod = paymentInfo.getMethod().getType();
                // 가상 계좌가 발급된 상태입니다. 관련 로직을 여기에 구현하세요.
                break;
            }
            case "PAID": {
                // 모든 금액을 지불했습니다! 결제 완료 후 처리할 로직을 여기에 구현하세요.
                log.info("성공!!!!");
                break;
            }
            default:
                // 결제 상태가 다른 경우
                orderRepository.deleteById(orderId);
                break;
        }
    }

}