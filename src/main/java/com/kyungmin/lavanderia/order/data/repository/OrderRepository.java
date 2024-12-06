package com.kyungmin.lavanderia.order.data.repository;

import com.kyungmin.lavanderia.order.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
