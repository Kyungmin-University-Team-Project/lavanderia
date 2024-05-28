package com.kyungmin.lavanderia.member.data.entity;

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
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_NO")
    private Long addressNo; // 주소 번호

    @Column(name = "MEMBER_ID")
    private String memberId; // 회원 아이디

    @Column(name = "ADDRESS")
    private String address; // 주소

    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress; // 상세 주소

    @Column(name = "RECEIVER")
    private String receiver; // 수신인 이름

    @Column(name = "PHONE")
    private String phone; // 전화번호

    @Column(name = "REQUEST")
    private String request; // 요청사항

    @Column(name = "DEFAULT_YN")
    private String defaultYn; // 기본 주소 여부

    @Builder
    public AddressEntity(Long addressNo, String memberId, String address, String detailAddress, String receiver, String phone, String request, String defaultYn) {
        this.addressNo = addressNo;
        this.memberId = memberId;
        this.address = address;
        this.detailAddress = detailAddress;
        this.receiver = receiver;
        this.phone = phone;
        this.request = request;
        this.defaultYn = defaultYn;
    }
}
