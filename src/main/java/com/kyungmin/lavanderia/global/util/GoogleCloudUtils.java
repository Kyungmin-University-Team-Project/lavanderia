package com.kyungmin.lavanderia.global.util;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GoogleCloudUtils {

    private static String bucketName;

    private static Storage storage;

    @Autowired
    public GoogleCloudUtils(Storage storage, @Value("${spring.cloud.bucket}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    public static String uploadSingleFile(MultipartFile imgFile) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String ext = imgFile.getOriginalFilename() != null ?
                FilenameUtils.getExtension(imgFile.getOriginalFilename()) : "jpg"; // 파일 확장자 가져오기

        // Cloud에 이미지 업로드
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, uuid + "." + ext) // UUID 뒤에 확장자 추가
                        .setContentType(imgFile.getContentType())
                        .build(),
                imgFile.getInputStream()
        );

        return blobInfo.getMediaLink();

    }

    public static List<String> uploadListFile(List<MultipartFile> imgFiles) throws IOException {

        List<String> imageUrls = new ArrayList<>();

        for(MultipartFile file : imgFiles) {
            imageUrls.add(uploadSingleFile(file));
        }

        return imageUrls;
    }


    @Getter
    public static String GOOGLE_IMAGE_CLOUD_URL = "https://storage.googleapis.com/lavanderia_img/";

    // 이미지 업로드
    public static void uploadImageById(String imageId, MultipartFile image) {
        try {
            storage.create(
                    BlobInfo.newBuilder(bucketName, imageId)
                            .setContentType(image.getContentType())
                            .build(),
                    image.getInputStream()
            );
        } catch (IOException e) {
            throw new RuntimeException("클라우드에 이미지 업로드 실패", e);
        }
    }

    // 이미지 삭제
    public static void deleteImageById(String imageId) {
        try {
            BlobId blobId = BlobId.of(bucketName, imageId);
            boolean result = storage.delete(blobId);
            if (!result) {
                throw new RuntimeException("클라우드에서 이미지 삭제 실패");
            }
        } catch (StorageException e) {
            throw new RuntimeException("클라우드에서 이미지 삭제 실패", e);
        }
    }

    // 이미지 업데이트
    public static void updateImageById(String imageId, MultipartFile image) {
        deleteImageById(imageId);
        uploadImageById(imageId, image);
    }

}
