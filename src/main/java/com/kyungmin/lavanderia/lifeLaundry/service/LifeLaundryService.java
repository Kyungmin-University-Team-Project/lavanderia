package com.kyungmin.lavanderia.lifeLaundry.service;

import com.kyungmin.lavanderia.lifeLaundry.data.dto.LifeLaundryDTO;

import java.util.List;

public interface LifeLaundryService {
    void addLifeLaundry(LifeLaundryDTO.InsertLifeLaundryDTO insertLifeLaundryDTO, String memberId);

    void deleteLifeLaundry(Long repairCartId, String memberId);

    List<LifeLaundryDTO.LifeLaundryCartResponseDTO> getLifeLaundryCartList(String memberId);
}
