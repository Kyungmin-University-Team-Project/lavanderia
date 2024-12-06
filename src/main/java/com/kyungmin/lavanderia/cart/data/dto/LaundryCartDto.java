package com.kyungmin.lavanderia.cart.data.dto;

import com.kyungmin.lavanderia.cart.data.entity.LaundryCart;
import com.kyungmin.lavanderia.laundry.data.entity.Laundry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaundryCartDto {

    @Schema(description = "세탁물 카트 ID")
    private Long laundryCartId;

    @Schema(description = "세탁물 ID")
    private Long laundryId;

    @Schema(description = "세탁물 이미지 URL")
    private String imgUrl;

    @Schema(description = "세탁물 가격")
    private Integer price;

    @Schema(description = "세탁물 종류 (드레스, 셔츠, 바지, 니트)")
    private String type;

    public static LaundryCartDto from(LaundryCart laundryCart) {
        Laundry laundry = laundryCart.getLaundry();
        return LaundryCartDto.builder()
            .laundryCartId(laundryCart.getLaundryCartId())
            .laundryId(laundry.getLaundryId())
            .imgUrl(laundry.getImgUrl())
            .price(laundry.getPrice())
            .type(laundry.getType())
            .build();
    }
}
