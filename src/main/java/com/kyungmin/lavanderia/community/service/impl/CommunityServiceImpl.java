package com.kyungmin.lavanderia.community.service.impl;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.kyungmin.lavanderia.community.data.dto.CommunityDTO;
import com.kyungmin.lavanderia.community.data.entity.Community;
import com.kyungmin.lavanderia.community.repository.CommunityRepository;
import com.kyungmin.lavanderia.community.service.CommunityService;
import com.kyungmin.lavanderia.member.data.entity.Member;
import com.kyungmin.lavanderia.member.data.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    @Value("${spring.cloud.bucket}") // application.yml에 써둔 bucket 이름
    private String bucketName;

    private final Storage storage;

    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;

    @Override
    public void save(CommunityDTO communityDTO, String memberId) throws IOException {

        String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
        String ext = communityDTO.getImage().getContentType(); // 파일의 형식 ex) JPG

        // Cloud에 이미지 업로드
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, uuid)
                        .setContentType(ext)
                        .build(),
                communityDTO.getImage().getInputStream()
        );

        Optional<Member> member = memberRepository.findById(memberId);

        Community community = communityDTO.toEntity(member.get(), blobInfo.getMediaLink());

        communityRepository.save(community);
    }

    @Override
    public Page<Community> findAll(Pageable pageable) {
        return communityRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id, String memberId) {

            Optional<Member> member = memberRepository.findById(memberId);

            Optional<Community> community = communityRepository.findById(id);

            if (community.get().getMember().getMemberId().equals(member.get().getMemberId())) {
                communityRepository.deleteById(id);
            }
    }

    @Override
    public void update(Long id, CommunityDTO communityDTO, String memberId) {

            Optional<Member> member = memberRepository.findById(memberId);

            Optional<Community> community = communityRepository.findById(id);

            if (community.get().getMember().getMemberId().equals(member.get().getMemberId())) {
                community.get().setTitle(communityDTO.getTitle());
                community.get().setContent(communityDTO.getContent());
                community.get().setCategory(communityDTO.getCategory());
                communityRepository.save(community.get());
            }
    }

    @Override
    public Page<Community> findByCategory(String category, Pageable pageable) {
        return communityRepository.findByCategory(category, pageable);
    }

}