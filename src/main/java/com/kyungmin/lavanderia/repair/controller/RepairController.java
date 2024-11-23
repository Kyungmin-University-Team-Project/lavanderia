package com.kyungmin.lavanderia.repair.controller;

import com.kyungmin.lavanderia.repair.data.dto.RepairDTO;
import com.kyungmin.lavanderia.repair.service.RepairService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "수선 API")
@RequestMapping("/repair")
@RequiredArgsConstructor
@RestController
public class RepairController {

    private final RepairService repairService;

    @PostMapping("/add")
    @Operation(summary = "옷 수선 장바구니 추가", description = "옷 수선을 장바구니에 추가합니다.<br><br> " +
            "Post 요청, access 토큰 필요<br><br>필요 타입<br> " +
            "clothesType(String): 옷 종류<br> " + "howTo(String): 어떻게<br> " + "detailInfo(String): 상세 정보<br> " + "request(String): 요청 사항<br> " + "price(number): 가격")
    public ResponseEntity<?> addRepair(@RequestBody RepairDTO.InsertRepairDTO insertRepairDTO) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            log.info("{} 수선 추가", memberId);
            repairService.addRepair(memberId, insertRepairDTO);
            return ResponseEntity.ok(memberId + " 수선 추가 성공");
        } catch (Exception e) {
            log.error("{} 수선 추가 실패: {}", memberId, e.getMessage());
            return ResponseEntity.badRequest().body("수선 추가 실패: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @Operation(summary = "옷 수선 장바구니 삭제", description = "옷 수선을 장바구니에서 삭제합니다.<br><br> " +
            "Post 요청, access 토큰 필요<br><br>필요 타입<br> " + "repairCartId(number): 옷 수선 장바구니 ID")
    public ResponseEntity<?> deleteRepair(@RequestBody RepairDTO.CartIdDTO cartIdDTO) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            repairService.deleteRepair(memberId, cartIdDTO.getRepairCartId());
            log.info("{} {}수선 삭제", memberId, cartIdDTO.getRepairCartId());
            return ResponseEntity.ok(memberId + " 수선 삭제 성공");
        } catch (Exception e) {
            log.error("{} {}수선 삭제 실패: {}", memberId, cartIdDTO.getRepairCartId(), e.getMessage());
            return ResponseEntity.badRequest().body("수선 삭제 실패: " + e.getMessage());
        }
    }

    @PostMapping("/cart-list")
    @Operation(summary = "옷 수선 카트 리스트 조회", description = "옷 수선 카트 리스트를 조회합니다.<br><br> " +
            "Post 요청, access 토큰 필요<br> " +
            "access 토큰만 보내면 됩니다.")
    public ResponseEntity<?> getRepairList() {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            List<RepairDTO.RepairCartResponseDTO> repairList = repairService.findRepairsByMemberId(memberId);
            log.info("{}의 수선 리스트 조회 성공", memberId);
            return ResponseEntity.ok(repairList);
        } catch (Exception e) {
            log.error("{}의 수선 리스트 조회 실패: {}", memberId, e.getMessage());
            return ResponseEntity.badRequest().body("수선 리스트 조회 실패: " + e.getMessage());
        }
    }
}