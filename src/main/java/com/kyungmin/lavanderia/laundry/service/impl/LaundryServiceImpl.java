package com.kyungmin.lavanderia.laundry.service.impl;

import com.kyungmin.lavanderia.global.util.GoogleCloudUtils;
import com.kyungmin.lavanderia.laundry.data.dto.LaundryDto.LaundryInsert;
import com.kyungmin.lavanderia.laundry.data.entity.Laundry;
import com.kyungmin.lavanderia.laundry.mapper.LaundryMapper;
import com.kyungmin.lavanderia.laundry.repository.LaundryRepository;
import com.kyungmin.lavanderia.laundry.service.LaundryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LaundryServiceImpl implements LaundryService {

    private final LaundryRepository laundryRepository;

    @Override
    public void addLaundra(List<LaundryInsert> laundryInserts, List<MultipartFile> laundryImages) throws IOException {

        if(laundryInserts.size() != laundryImages.size()) {
            throw new IllegalArgumentException("세탁물 정보와 이미지 정보가 일치하지 않습니다.");
        }

        for(int i = 0; i < laundryInserts.size(); i++) {
            LaundryInsert laundry = laundryInserts.get(i);
            MultipartFile laundryImage = laundryImages.get(i);

            String imageUrl = GoogleCloudUtils.uploadSingleFile(laundryImage);

            Laundry laundryEntity = LaundryMapper.INSTANCE.toEntity(laundry, imageUrl);

            laundryRepository.save(laundryEntity);
        }

    }
}
