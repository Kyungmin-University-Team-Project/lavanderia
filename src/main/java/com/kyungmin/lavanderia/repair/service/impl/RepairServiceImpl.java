package com.kyungmin.lavanderia.repair.service.impl;

import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.member.data.repository.MemberRepository;
import com.kyungmin.lavanderia.repair.data.dto.RepairDTO;
import com.kyungmin.lavanderia.repair.data.entity.Repair;
import com.kyungmin.lavanderia.repair.data.entity.RepairCart;
import com.kyungmin.lavanderia.repair.data.repository.RepairCartRepository;
import com.kyungmin.lavanderia.repair.data.repository.RepairRepository;
import com.kyungmin.lavanderia.repair.service.RepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepairServiceImpl implements RepairService {

    private final RepairRepository repairRepository;
    private final RepairCartRepository repairCartRepository;
    private final MemberRepository memberRepository;

    @Override
    public void addRepair(String memberId, RepairDTO.InsertRepairDTO insertRepairDTO) {
        try {
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

            Repair repair = Repair.builder()
                    .clothesType(insertRepairDTO.getClothesType())
                    .howTo(insertRepairDTO.getHowTo())
                    .request(insertRepairDTO.getRequest())
                    .price(insertRepairDTO.getPrice())
                    .detailInfo(insertRepairDTO.getDetailInfo())
                    .member(member)
                    .build();
            repairRepository.save(repair);

            RepairCart repairCart = RepairCart.builder()
                    .member(member)
                    .repair(repair)
                    .build();
            repairCartRepository.save(repairCart);
        }catch (Exception e){
            throw new IllegalArgumentException("수선 추가 실패: " + e.getMessage());
        }


    }

    @Override
    public void deleteRepair(String memberId, Long repairCartId) {
        try {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

            RepairCart repairCart = repairCartRepository.findById(repairCartId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 수선입니다."));

            if (!repairCart.getMember().equals(member)) {
                throw new IllegalArgumentException("해당 수선이 이 회원의 것이 아닙니다.");
            }

            // JPA delete 메서드 호출
            repairCartRepository.delete(repairCart);
        } catch (Exception e) {
            throw new IllegalArgumentException("수선 삭제 실패: " + e.getMessage());
        }
    }

    @Override
    public List<RepairDTO.RepairCartResponseDTO> findRepairsByMemberId(String memberId) {
        List<RepairCart> repairCarts = repairCartRepository.findRepairsByMemberId(memberId);

        return repairCarts.stream()
                .map(rc -> RepairDTO.RepairCartResponseDTO.builder()
                        .repairCartId(rc.getRepairCartId())
                        .repairId(rc.getRepair().getRepairId())
                        .memberId(rc.getRepair().getMember().getMemberId())
                        .clothesType(rc.getRepair().getClothesType())
                        .howTo(rc.getRepair().getHowTo())
                        .detailInfo(rc.getRepair().getDetailInfo())
                        .request(rc.getRepair().getRequest())
                        .price(rc.getRepair().getPrice())
                        .build())
                .toList();
    }


}