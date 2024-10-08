package com.kyungmin.lavanderia.order.mapper;

import com.kyungmin.lavanderia.order.data.dto.OrderDTO.OrderInsert;
import com.kyungmin.lavanderia.order.data.entity.Order;
import com.kyungmin.lavanderia.product.data.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toEntity(String memberId, OrderInsert orderDTO);

    // 추가: UUID 값을 Product로 매핑하는 메소드
    @Mapping(target = "productId", source = "productId") // productId가 UUID인 경우, Product 객체로 설정
    Product map(UUID productId);

}