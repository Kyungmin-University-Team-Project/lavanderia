package com.kyungmin.lavanderia.address.service;

import com.kyungmin.lavanderia.address.data.dto.AddressDTO;
import com.kyungmin.lavanderia.address.data.dto.AddressInsertDTO;


import java.util.List;

public interface AddressService {
    void addressInsert(String memberId, AddressInsertDTO addressInsertDTO);
    void addressUpdate(String memberId, AddressDTO addressUpdateDTO);
    void addressDelete(String memberId, int addressId);
    AddressDTO findAddressDtoByMemberId(String memberId);
    List<AddressDTO> findAllByMemberId(String memberId);
}
