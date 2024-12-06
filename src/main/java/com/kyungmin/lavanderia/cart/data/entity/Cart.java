package com.kyungmin.lavanderia.cart.data.entity;

import com.kyungmin.lavanderia.laundry.data.entity.Laundry;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.product.data.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_CART")
public class Cart {

    @Id
    @Column(name = "CART_ID")
    private String cartId;

    @Column(name = "QUANTITY")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member memberId;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "LAUNDRY_ID")
    private Laundry laundry;

}