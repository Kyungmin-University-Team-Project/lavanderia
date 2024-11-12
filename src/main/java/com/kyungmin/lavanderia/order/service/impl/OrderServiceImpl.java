package com.kyungmin.lavanderia.order.service.impl;

import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.order.data.dto.OrderDTO.OrderInsert;
import com.kyungmin.lavanderia.order.data.entity.Order;
import com.kyungmin.lavanderia.order.data.entity.OrderDetail;
import com.kyungmin.lavanderia.order.data.repository.OrderDetailRepository;
import com.kyungmin.lavanderia.order.data.repository.OrderRepository;
import com.kyungmin.lavanderia.order.mapper.OrderMapper;
import com.kyungmin.lavanderia.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LogManager.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public Long insertOrder(Member member, OrderInsert orderDTO) {

        Order order = OrderMapper.INSTANCE.toEntity(member, orderDTO);

        orderRepository.save(order);

        for (OrderDetail orderDetail : order.getOrderDetailList())
            orderDetail.setOrderId(order);

        orderDetailRepository.saveAll(order.getOrderDetailList());

        return order.getOrderId();
    }
}
