package com.kyungmin.lavanderia.order.service.impl;

import com.kyungmin.lavanderia.laundry.data.entity.Laundry;
import com.kyungmin.lavanderia.laundry.repository.LaundryRepository;
import com.kyungmin.lavanderia.lifeLaundry.data.entity.LifeLaundry;
import com.kyungmin.lavanderia.lifeLaundry.data.repository.LifeLaundryRepository;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.order.data.dto.OrderDTO.InsertOrder;
import com.kyungmin.lavanderia.order.data.entity.Order;
import com.kyungmin.lavanderia.order.data.entity.OrderDetail;
import com.kyungmin.lavanderia.order.data.repository.OrderDetailRepository;
import com.kyungmin.lavanderia.order.data.repository.OrderRepository;
import com.kyungmin.lavanderia.order.mapper.OrderMapper;
import com.kyungmin.lavanderia.order.service.OrderService;
import com.kyungmin.lavanderia.repair.data.entity.Repair;
import com.kyungmin.lavanderia.repair.data.repository.RepairRepository;
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

    private final LifeLaundryRepository lifeLaundryRepository;
    private final RepairRepository repairRepository;
    private final LaundryRepository laundryRepository;

    @Override
    public Long insertOrder(Member member, InsertOrder orderDTO) {

        Order order = OrderMapper.INSTANCE.toEntity(member, orderDTO);

        orderRepository.save(order);

        if (orderDTO.getLifeLaundryIdList() != null){
            LifeLaundry lifeLaundry;
            for (Long id : orderDTO.getLifeLaundryIdList()) {
                lifeLaundry = lifeLaundryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 생활 빨래입니다."));
                lifeLaundry.setOrder(order);
                lifeLaundryRepository.save(lifeLaundry);
            }
        }

        if (orderDTO.getRepairIdList() != null){
            Repair repair;
            for (Long id : orderDTO.getRepairIdList()) {
                repair = repairRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 옷 수선입니다."));
                repair.setOrder(order);
                repairRepository.save(repair);
            }
        }

        if (orderDTO.getLaundryIdList() != null){
            Laundry laundry;
            for (Long id : orderDTO.getLaundryIdList()) {
                laundry = laundryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 세탁물입니다."));
                laundry.setOrder(order);
                laundryRepository.save(laundry);
            }
        }

        if (orderDTO.getOrderDetailList() != null ) {
            for (OrderDetail orderDetail : order.getOrderDetailList()) {
                orderDetail.setOrderId(order);
            }
            orderDetailRepository.saveAll(order.getOrderDetailList());
        }

        return order.getOrderId();
    }
}
