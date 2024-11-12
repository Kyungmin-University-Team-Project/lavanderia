package com.kyungmin.lavanderia.order.service;

import com.kyungmin.lavanderia.order.data.entity.OrderDetail;

public interface OrderDetailService {
    OrderDetail getOrderDetail(Long orderId);
}
