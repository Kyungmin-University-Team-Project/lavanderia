package com.kyungmin.lavanderia.address.controller;

import com.kyungmin.lavanderia.address.data.dto.AddressDTO;
import com.kyungmin.lavanderia.address.data.dto.AddressInsertDTO;
import com.kyungmin.lavanderia.address.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "주소 API")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/address-insert")
    @Operation(summary = "주소 추가", description = "주소 정보를 받아 추가합니다")
    public ResponseEntity<String> addressInsert(@RequestBody AddressInsertDTO addressInsertDTO) {

        HttpStatus httpStatus;
        String result;

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            addressService.addressInsert(memberId, addressInsertDTO);
            httpStatus = HttpStatus.OK;
            result = "주소 추가 완료";
        } catch (UsernameNotFoundException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = e.getMessage();
        }

        return response(httpStatus, result);
    }

    @PostMapping("/address-update")
    @Operation(summary = "주소 수정", description = "주소 정보를 받아 수정합니다")
    public ResponseEntity<String> addressUpdate(@RequestBody AddressDTO addressUpdateDTO) {

        HttpStatus httpStatus;
        String result;

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            addressService.addressUpdate(memberId, addressUpdateDTO);
            httpStatus = HttpStatus.OK;
            result = "수정 수정 완료";
        } catch (UsernameNotFoundException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = e.getMessage();
        }

        return response(httpStatus, result);
    }

    @PostMapping("/address-delete")
    @Operation(summary = "주소 삭제", description = "주소 아이디를 받아 주소를 삭제합니다")
    public ResponseEntity<String> addressDelete(@RequestBody int addressId) {

        HttpStatus httpStatus;
        String result;

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            addressService.addressDelete(memberId, addressId);
            httpStatus = HttpStatus.OK;
            result = "주소 삭제 완료";
        } catch (UsernameNotFoundException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = e.getMessage();
        }

        return response(httpStatus, result);
    }

    @PostMapping("/address")
    @Operation(summary = "기본 주소 조회", description = "토큰을 주면 기본 주소를 조회합니다")
    public AddressDTO address() {

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

        return addressService.findAddressDtoByMemberId(memberId);
    }

    @PostMapping("/address-list")
    @Operation(summary = "모든 주소 조회", description = "토큰을 주면 모든 주소를 조회합니다")
    public List<AddressDTO> addressList() {

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

        return addressService.findAllByMemberId(memberId);
    }

    private ResponseEntity<String> response(HttpStatus httpStatus,String result) {
        return ResponseEntity.status(httpStatus)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
                .body(result);
    }
}
