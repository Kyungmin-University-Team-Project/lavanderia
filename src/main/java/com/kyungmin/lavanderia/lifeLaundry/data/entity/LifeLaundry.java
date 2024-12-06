package com.kyungmin.lavanderia.lifeLaundry.data.entity;

import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.order.data.entity.Order;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_LIFE_LAUNDRY")
public class LifeLaundry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIFE_LAUNDRY_ID")
    private Long lifeLaundryId;

    @Column(name = "TYPE")
    private String type; // 상품 설명 (예: "3.5KG 미만", "3.5KG 이상")

    @Column(name = "WEIGHT")
    private double weight; // 상품 무게 (예: 3.5)

    @Column(name = "PRICE")
    private int price; // 상품 가격 (예: 13500)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;
}