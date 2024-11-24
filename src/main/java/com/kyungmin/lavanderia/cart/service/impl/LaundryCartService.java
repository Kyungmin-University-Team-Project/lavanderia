package com.kyungmin.lavanderia.cart.service.impl;

import com.kyungmin.lavanderia.cart.data.dto.LaundryCartDto;
import com.kyungmin.lavanderia.cart.data.entity.LaundryCart;
import com.kyungmin.lavanderia.cart.repository.LaundryCartRepository;
import com.kyungmin.lavanderia.laundry.data.entity.Laundry;
import com.kyungmin.lavanderia.member.data.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaundryCartService {

    private final LaundryCartRepository laundryCartRepository;

    public List<LaundryCartDto> getAllCart(Member member) {
        List<LaundryCart> laundryCarts = laundryCartRepository.findAllByMember(member);
        return laundryCarts.stream()
                .map(LaundryCartDto::from) // LaundryCartDTO로 변환
                .collect(Collectors.toList());
    }

    public void deleteCart(Member member, Long cartId) {
        LaundryCart laundryCart = laundryCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카트가 존재하지 않습니다."));

        if (!laundryCart.getMember().getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("해당 카트에 접근 권한이 없습니다.");
        }

        laundryCartRepository.delete(laundryCart);
    }

    public void deleteAllCart(List<Laundry> laundryList) {
        try {
            laundryList.forEach(laundry -> {
                laundryCartRepository.deleteByLaundry(laundry);
            });
        }catch (Exception e){
            throw new IllegalArgumentException("생활 빨래 장바구니 삭제 실패: " + e.getMessage());
        }
    }
}
