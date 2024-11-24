package com.kyungmin.lavanderia.laundry.data.entity;

import com.kyungmin.lavanderia.cart.data.entity.LaundryCart;
import com.kyungmin.lavanderia.order.data.entity.OrderDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_LAUNDRY")
public class Laundry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LAUNDRY_ID")
    private Long laundryId;

    @Column(name = "IMG_URL")
    private String imgUrl;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "TYPE")
    private String type;

    @OneToMany(mappedBy = "laundry")
    private List<OrderDetail> orderDetailList;

    @OneToMany(mappedBy = "laundry")
    private List<LaundryCart> laundryCart;

    /*@OneToMany(mappedBy = "laundry")
    private List<Cart> cart;*/

}
