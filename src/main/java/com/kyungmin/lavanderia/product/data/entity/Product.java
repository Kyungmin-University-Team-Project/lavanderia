package com.kyungmin.lavanderia.product.data.entity;

import com.kyungmin.lavanderia.cart.data.entity.Cart;
import com.kyungmin.lavanderia.order.data.entity.OrderDetail;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_PRODUCT")
@ToString(exclude = {"productImgUrl", "orderDetaiList", "cartList"})
public class Product {

    @Id @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "PRODUCT_ID", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID productId; // 상품 아이디

    @Column(name = "PRODUCT_NAME")
    private String productName; // 상품 이름

    @Column(name = "PRODUCT_PRICE")
    private Long productPrice; // 상품 가격

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription; // 상품 설명

    @OneToMany(mappedBy = "productImgUrlId")
    private List<ProductImgUrl> productImgUrl; // 상품 이미지

    @OneToMany(mappedBy = "productId")
    private List<OrderDetail> orderDetailList;

    @OneToMany(mappedBy = "product")
    private List<Cart> cartList;

}