package com.subhajit.ImageUploader.Controller;

import com.subhajit.ImageUploader.Service.Impl.S3ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/aws")
public class ImageUploadController {
    @Autowired
    S3ImageUploader s3ImageUploader;
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(MultipartFile multipartFile)
    {
        String url=s3ImageUploader.uploadImage(multipartFile);
        return ResponseEntity.ok(url);
    }
    @GetMapping("/allFiles")
    public ResponseEntity<?> getAllFiles()
    {
        List<String> urls=s3ImageUploader.allFiles();
        return ResponseEntity.ok(urls);
    }
    @GetMapping("/{name}")
    public ResponseEntity<?> getImageUrlByName(@PathVariable("name")  String name){
        String url=s3ImageUploader.getImageUrlByName(name);
        return ResponseEntity.ok(url);
    }
    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteImageByName(@PathVariable("name")  String name){
        String msg=s3ImageUploader.deleteImageByName(name);
        return ResponseEntity.ok(msg);
    }
}
