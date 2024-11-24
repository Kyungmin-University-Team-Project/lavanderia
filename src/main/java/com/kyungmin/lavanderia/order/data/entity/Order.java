package com.kyungmin.lavanderia.order.data.entity;

import com.kyungmin.lavanderia.laundry.data.entity.Laundry;
import com.kyungmin.lavanderia.lifeLaundry.data.entity.LifeLaundry;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.order.data.constant.DeliveryStatus;
import com.kyungmin.lavanderia.repair.data.entity.Repair;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_ORDER")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member memberId;

    @CreationTimestamp
    @Column(name = "ORDER_DATE", updatable = false)
    private LocalDateTime orderDate;

    @Column(name = "RECEIVER_NAME")
    private String rcvrName;

    @Column(name = "RECEIVER_PHONE")
    private String rcvrPhone;

    @Column(name = "RECEIVER_ADDRESS")
    private String rcvrAddress;

    @Column(name = "RECEIVER_DETAIL_ADDRESS")
    private String rcvrDetailAddress;

    @Column(name = "RECEIVER_POSTAL_CODE")
    private String rcvrPostalCode;

    @Column(name = "DELIVERY_REQUEST_MESSAGE")
    private String dlvrReqMessage;

    @Column(name = "DELIVERY_STATUS")
    private String dlvrReqStatus;

    @Column(name = "TOTAL_PRICE")
    private int totalPrice;

    // LifeLaundry와의 관계
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<LifeLaundry> lifeLaundryList;

    // Repair와의 관계
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Repair> repairList;

    // Laundry와의 관계
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Laundry> laundryList;

    @OneToMany(mappedBy = "orderId")
    private List<OrderDetail> orderDetailList;

    @PrePersist
    public void prePersist() {
        this.dlvrReqStatus = DeliveryStatus.READY.getStatus();
    }

}