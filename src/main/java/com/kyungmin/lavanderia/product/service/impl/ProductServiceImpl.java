package com.kyungmin.lavanderia.product.service.impl;

import com.kyungmin.lavanderia.global.util.GoogleCloundUtils;
import com.kyungmin.lavanderia.product.data.dto.ProductDTO;
import com.kyungmin.lavanderia.product.data.entity.Product;
import com.kyungmin.lavanderia.product.data.entity.ProductImgUrl;
import com.kyungmin.lavanderia.product.data.mapper.ProductMapper;
import com.kyungmin.lavanderia.product.data.repository.ProductImgUrlRepository;
import com.kyungmin.lavanderia.product.data.repository.ProductRepository;
import com.kyungmin.lavanderia.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImgUrlRepository productImgUrlRepository;

    @Override
    public void insertProduct(ProductDTO productDTO) throws IOException {

        List<String> imageUrls = GoogleCloundUtils.uploadListFile(productDTO.getProductImage());

        Product product = ProductMapper.INSTANCE.toEntity(productDTO);

        productRepository.save(product);

        for(String productImageUrl : imageUrls) {
            ProductImgUrl productImgUrl = ProductMapper.INSTANCE.toEntity(productImageUrl, product);
            productImgUrlRepository.save(productImgUrl);
        }

    }
}