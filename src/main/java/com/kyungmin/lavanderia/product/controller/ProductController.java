package com.kyungmin.lavanderia.product.controller;

import com.kyungmin.lavanderia.global.ResponseEntityUtil;
import com.kyungmin.lavanderia.product.data.dto.ProductDTO;
import com.kyungmin.lavanderia.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "상품 API")
@RequestMapping("/product")
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(ProductDTO productDTO) throws IOException {

        productService.insertProduct(productDTO);

        return ResponseEntityUtil.response("상품 정보가 추가 되었습니다.", HttpStatus.OK);
    }
}