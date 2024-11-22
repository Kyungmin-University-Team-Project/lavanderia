package com.kyungmin.lavanderia.repair.service;

import com.kyungmin.lavanderia.repair.data.dto.RepairDTO;
import com.kyungmin.lavanderia.repair.data.entity.RepairCart;

import java.util.List;

public interface RepairService {
    void addRepair(String memberId, RepairDTO.InsertRepairDTO insertRepairDTO);

    void deleteRepair(String memberId, Long repairCartId);

    List<RepairDTO.RepairCartResponseDTO> findRepairsByMemberId(String memberId);
}
