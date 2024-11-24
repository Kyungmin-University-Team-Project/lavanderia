package com.kyungmin.lavanderia.cart.data.entity;

import com.kyungmin.lavanderia.laundry.data.entity.Laundry;
import com.kyungmin.lavanderia.member.data.entity.Member;
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
@Table(name = "TBL_LAUNDRY_CART")
public class LaundryCart {

    @Id
    @Column(name = "LAUNDRY_CART_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long laundryCartId;

    @ManyToOne
    @JoinColumn(name = "LAUNDRY_ID")
    private Laundry laundry;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

}
