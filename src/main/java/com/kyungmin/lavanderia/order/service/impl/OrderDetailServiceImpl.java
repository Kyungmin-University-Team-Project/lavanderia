package com.kyungmin.lavanderia.order.service.impl;

import com.kyungmin.lavanderia.order.data.entity.OrderDetail;
import com.kyungmin.lavanderia.order.data.repository.OrderDetailRepository;
import com.kyungmin.lavanderia.order.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail getOrderDetail(Long orderId) {
        return orderDetailRepository.findById(orderId).orElse(null);
    }
}
