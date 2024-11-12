package com.kyungmin.lavanderia.laundry.mapper;

import com.kyungmin.lavanderia.laundry.data.dto.LaundryDto.LaundryInsert;
import com.kyungmin.lavanderia.laundry.data.entity.Laundry;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LaundryMapper {

    LaundryMapper INSTANCE = Mappers.getMapper(LaundryMapper.class);

    Laundry toEntity(LaundryInsert laundry, String imgUrl);

}
