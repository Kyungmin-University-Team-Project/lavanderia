package com.kyungmin.lavanderia.product.service;

import com.kyungmin.lavanderia.product.data.dto.ProductDTO;

import java.io.IOException;

public interface ProductService {
    void insertProduct(ProductDTO productDTO) throws IOException;
}
