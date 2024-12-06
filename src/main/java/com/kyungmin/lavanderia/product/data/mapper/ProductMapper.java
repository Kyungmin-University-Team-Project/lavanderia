package com.kyungmin.lavanderia.product.data.mapper;

import com.kyungmin.lavanderia.product.data.dto.ProductDTO;
import com.kyungmin.lavanderia.product.data.entity.Product;
import com.kyungmin.lavanderia.product.data.entity.ProductImgUrl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toEntity(ProductDTO productDTO);

    @Mapping(source = "productImgUrl", target = "productImgUrl") // URL 매j핑
    @Mapping(source = "product", target = "productId") // Product 객체 매핑
    ProductImgUrl toEntity(String productImgUrl, Product product);
}