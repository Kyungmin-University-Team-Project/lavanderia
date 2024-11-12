package com.kyungmin.lavanderia.address.data.entity;

import com.kyungmin.lavanderia.member.data.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
@Table(name = "TBL_ADDRESS")
public class Address {

    @Id
    @Column(name = "ADDRESS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId; // 주소 아이디

    @JoinColumn(name = "MEMBER_ID")
    @ManyToOne
    private Member memberId; // 멤버 아이디

    @Column(name = "ADDRESS_NAME")
    private String addressName; // 주소 이름

    @Column(name = "RECEIVER_NAME")
    private String receiverName; // 수령인 이름

    @Column(name = "RECEIVER_PHONE")
    private String receiverPhone; // 수령인 전화번호

    @Column(name = "DELIVERY_REQUEST_MESSAGE")
    private String deliveryRequestMessage; // 배송 요청 메시지

    @Column(name = "ADDRESS")
    private String address; // 주소

    @Column(name = "ADDRESS_DETAIL")
    private String addressDetail; // 상세 주소

    @Column(name = "POSTAL_CODE")
    private String postalCode; // 우편번호

    @Column(name = "ADDRESS_DEFAULT_YN")
    private char addressDefaultYn; // 기본 주소 여부

    @Builder
    public Address(Member memberId, String addressName, String receiverName, String receiverPhone, String deliveryRequestMessage, String address, String addressDetail, char addressDefaultYn) {
        this.memberId = memberId;
        this.addressName = addressName;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.deliveryRequestMessage = deliveryRequestMessage;
        this.address = address;
        this.addressDetail = addressDetail;
        this.addressDefaultYn = addressDefaultYn;
    }
}
