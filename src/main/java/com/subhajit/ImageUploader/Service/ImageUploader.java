package com.subhajit.ImageUploader.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploader {
    String uploadImage(MultipartFile image);
    List<String> allFiles();

    String preSignedUrl(String fileName);
    String getImageUrlByName(String fileName);
    String deleteImageByName(String fileName);
}
