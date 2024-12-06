package com.kyungmin.lavanderia.address.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressInsertDTO {

    @Schema(description = "주소 이름", example = "집")
    private String addressName; // 주소 이름
    @Schema(description = "수령인 이름", example = "홍길동")
    private String receiverName; // 수령인 이름
    @Schema(description = "수령인 전화번호", example = "010-1234-5678")
    private String receiverPhone; // 수령인 전화번호
    @Schema(description = "요청사항", example = "집 앞에 놔주세요.")
    private String dlvrReqMassage; // 요청사항
    @Schema(description = "주소", example = "경기 양주시 고읍남로 94 (광사동, 양주고읍 LH13단지 아파트)")
    private String address; // 주소
    @Schema(description = "상세 주소", example = "303동 603호")
    private String addressDetail; // 상세 주소
    @Schema(description = "기본 주소 여부", example = "Y")
    private char addressDefaultYn; // 기본 주소 여부
}
