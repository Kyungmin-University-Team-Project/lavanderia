package com.kyungmin.lavanderia.lifeLaundry.service.impl;

import com.kyungmin.lavanderia.lifeLaundry.data.dto.LifeLaundryDTO;
import com.kyungmin.lavanderia.lifeLaundry.data.entity.LifeLaundry;
import com.kyungmin.lavanderia.lifeLaundry.data.entity.LifeLaundryCart;
import com.kyungmin.lavanderia.lifeLaundry.data.repository.LifeLaundryCartRepository;
import com.kyungmin.lavanderia.lifeLaundry.data.repository.LifeLaundryRepository;
import com.kyungmin.lavanderia.lifeLaundry.service.LifeLaundryService;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LifeLaundryServiceImpl implements LifeLaundryService {

    private final LifeLaundryRepository lifeLaundryRepository;
    private final LifeLaundryCartRepository lifeLaundryCartRepository;
    private final MemberRepository memberRepository;

    @Override
    public void addLifeLaundry(LifeLaundryDTO.InsertLifeLaundryDTO insertLifeLaundryDTO, String memberId) {
        try {
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

            LifeLaundry lifeLaundry = LifeLaundry.builder()
                    .type(insertLifeLaundryDTO.getType())
                    .weight(insertLifeLaundryDTO.getWeight())
                    .price(insertLifeLaundryDTO.getPrice())
                    .build();
            lifeLaundryRepository.save(lifeLaundry);

            LifeLaundryCart lifeLaundryCart = LifeLaundryCart.builder()
                    .member(member)
                    .lifeLaundry(lifeLaundry)
                    .build();
            lifeLaundryCartRepository.save(lifeLaundryCart);
        }catch (Exception e){
            throw new IllegalArgumentException("생활 빨래 추가 실패: " + e.getMessage());
        }
    }

    @Override
    public void deleteLifeLaundry(Long repairCartId, String memberId) {
        try {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

            LifeLaundryCart lifeLaundryCart = lifeLaundryCartRepository.findById(repairCartId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 생활 빨래입니다."));

            if (!lifeLaundryCart.getMember().equals(member)) {
                throw new IllegalArgumentException("해당 생활 빨래가 이 회원의 것이 아닙니다.");
            }

            lifeLaundryCartRepository.delete(lifeLaundryCart);
        }catch (Exception e){
            throw new IllegalArgumentException("생활 빨래 삭제 실패: " + e.getMessage());
        }
    }

    @Override
    public List<LifeLaundryDTO.LifeLaundryCartResponseDTO> getLifeLaundryCartList(String memberId) {
        try {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

            List<LifeLaundryCart> lifeLaundryCartList = member.getLifeLaundryCarts();

            return lifeLaundryCartList.stream()
                    .map(lifeLaundryCart -> LifeLaundryDTO.LifeLaundryCartResponseDTO.builder()
                            .lifeLaundryCartId(lifeLaundryCart.getLifeLaundryCartId())
                            .lifeLaundryId(lifeLaundryCart.getLifeLaundry().getLifeLaundryId())
                            .memberId(lifeLaundryCart.getMember().getMemberId())
                            .type(lifeLaundryCart.getLifeLaundry().getType())
                            .weight(lifeLaundryCart.getLifeLaundry().getWeight())
                            .price(lifeLaundryCart.getLifeLaundry().getPrice())
                            .build())
                    .toList();
        }catch (Exception e){
            throw new IllegalArgumentException("생활 빨래 장바구니 목록 조회 실패: " + e.getMessage());
        }
    }


}