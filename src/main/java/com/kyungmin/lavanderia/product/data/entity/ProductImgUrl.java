package com.kyungmin.lavanderia.product.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TBL_PRODUCT_IMG_URL")
public class ProductImgUrl {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "PRODUCT_IMG_URL_ID")
        private Long productImgUrlId; // 상품 이미지 아이디

        @Column(name = "PRODUCT_IMG_URL")
        private String productImgUrl; // 상품 이미지 URL

        @ManyToOne
        @JoinColumn(name = "PRODUCT_ID")
        private Product productId; // 상품 이미지 URL

}