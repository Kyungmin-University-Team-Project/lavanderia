package com.kyungmin.lavanderia.address.service.impl;

import com.kyungmin.lavanderia.address.data.dto.AddressDTO;
import com.kyungmin.lavanderia.address.data.dto.AddressInsertDTO;
import com.kyungmin.lavanderia.address.data.entity.Address;
import com.kyungmin.lavanderia.address.data.repository.AddressRepository;
import com.kyungmin.lavanderia.address.service.AddressService;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;
    @Override
    public void addressInsert(String memberId, AddressInsertDTO addressInsertDTO) {

        if(addressRepository.existsByMemberId(findMemberByMemberId(memberId)) && addressInsertDTO.getAddressDefaultYn() == 'Y') {
                changeDefaultAddress(memberId);
        }

        // 주소 추가
        addressRepository.save(Address.builder()
                .memberId(findMemberByMemberId(memberId))
                .addressName(addressInsertDTO.getAddressName())
                .receiverName(addressInsertDTO.getReceiverName())
                .receiverPhone(addressInsertDTO.getReceiverPhone())
                .deliveryRequestMessage(addressInsertDTO.getDlvrReqMassage())
                .address(addressInsertDTO.getAddress())
                .addressDetail(addressInsertDTO.getAddressDetail())
                .addressDefaultYn(addressInsertDTO.getAddressDefaultYn())
                .build()
        );

    }

    @Override
    public void addressUpdate(String memberId, AddressDTO addressUpdateDTO) {
        Address address = findByMemberIdAndAddressId(memberId, addressUpdateDTO.getAddressId());

        // 주소 수정
        address.setAddressName(addressUpdateDTO.getAddressName());
        address.setReceiverName(addressUpdateDTO.getReceiverName());
        address.setReceiverPhone(addressUpdateDTO.getReceiverPhone());
        address.setDeliveryRequestMessage(addressUpdateDTO.getDeliveryRequestMessage());
        address.setAddress(addressUpdateDTO.getAddress());
        address.setAddressDetail(addressUpdateDTO.getAddressDetail());
        address.setAddressDefaultYn(addressUpdateDTO.getAddressDefaultYn());

        if (addressUpdateDTO.getAddressDefaultYn() == 'Y') {
            changeDefaultAddress(memberId);
        }

    }

    @Override
    public void addressDelete(String memberId, int addressId) {
        Address address = findByMemberIdAndAddressId(memberId, addressId);

        if (address.getAddressDefaultYn() == 'Y') {
            throw new UsernameNotFoundException("기본 주소는 삭제할 수 없습니다.");
        }

        // 주소 삭제
        addressRepository.delete(address);
    }

    @Override
    public AddressDTO findAddressDtoByMemberId(String memberId) {
        // 주소 조회
        Address address = addressRepository.findByMemberIdAndAddressDefaultYn(findMemberByMemberId(memberId), 'Y')
                .orElseThrow(() -> new UsernameNotFoundException("주소를 찾을 수 없습니다."));

        return AddressDTO.builder()
                .addressId(address.getAddressId())
                .addressName(address.getAddressName())
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .deliveryRequestMessage(address.getDeliveryRequestMessage())
                .address(address.getAddress())
                .addressDetail(address.getAddressDetail())
                .addressDefaultYn(address.getAddressDefaultYn())
                .build();
    }

    @Override
    public List<AddressDTO> findAllByMemberId(String memberId) {
        List<Address> addressList = addressRepository.findAllByMemberId(findMemberByMemberId(memberId));

        return addressList.stream()
                .map(address -> AddressDTO.builder()
                        .addressId(address.getAddressId())
                        .addressName(address.getAddressName())
                        .receiverName(address.getReceiverName())
                        .receiverPhone(address.getReceiverPhone())
                        .deliveryRequestMessage(address.getDeliveryRequestMessage())
                        .address(address.getAddress())
                        .addressDetail(address.getAddressDetail())
                        .addressDefaultYn(address.getAddressDefaultYn())
                        .build())
                .toList();
    }

    // 주소 회원 권한 확인
    Address findByMemberIdAndAddressId(String memberId, int addressId) {
        return addressRepository.findByMemberIdAndAddressId(findMemberByMemberId(memberId), addressId)
                .orElseThrow(() -> new UsernameNotFoundException("주소를 찾을 수 없습니다."));
    }

    // 회원 아이디로 회원 찾기
    Member findMemberByMemberId(String memberId) {
        Optional<Member> member = memberRepository.findMemberByMemberId(memberId);
        if (member.isEmpty()) {
            throw new UsernameNotFoundException(memberId + "회원을 찾을 수 없습니다.");
        }
        return member.get();
    }

    // 기존 Y인 기본 주소 N으로 변경
    void changeDefaultAddress(String memberId) {
        Address address = addressRepository.findByMemberId(findMemberByMemberId(memberId));
        address.setAddressDefaultYn('N');
        addressRepository.save(address);
    }
}
