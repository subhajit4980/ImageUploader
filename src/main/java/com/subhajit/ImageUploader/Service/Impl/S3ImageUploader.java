package com.subhajit.ImageUploader.Service.Impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.subhajit.ImageUploader.Exception.ImageUploadException;
import com.subhajit.ImageUploader.Service.ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Service
public class S3ImageUploader implements ImageUploader {
    @Autowired
   private AmazonS3 client;
    @Value("${app.s2.bucket}")
    private String bucketName;
    @Override
    public String uploadImage(MultipartFile image) {
        String actualFileName = image.getOriginalFilename();
        assert actualFileName != null;
        String UploadedFileName = UUID.randomUUID().toString()+actualFileName.substring(actualFileName.lastIndexOf("."));
        ObjectMetadata metaData=new ObjectMetadata();
        metaData.setContentLength(image.getSize());
        try {
            PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucketName,UploadedFileName,image.getInputStream(),metaData));
            return this.preSignedUrl(UploadedFileName);
        } catch (IOException e) {
            throw new ImageUploadException("error in uploading image "+e.getMessage());
        }
    }

    @Override
    public List<String> allFiles() {
        ListObjectsV2Request localObjectReqiest=new ListObjectsV2Request().withBucketName(bucketName);
         ListObjectsV2Result listObjectsV2Result= client.listObjectsV2(localObjectReqiest);
         List<S3ObjectSummary> objectSummaries=listObjectsV2Result.getObjectSummaries();
//         return objectSummaries.stream().map(S3ObjectSummary::getKey).toList();
        return objectSummaries.stream().map(item-> this.preSignedUrl(item.getKey())).toList();
    }

    @Override
    public String preSignedUrl(String fileName) {
        Date expirationData =new Date();
        long time = expirationData.getTime();
        int hour=2;
        time+=(hour*60*60*1000);
        expirationData.setTime(time);
        GeneratePresignedUrlRequest generatePresignedUrlRequest=new GeneratePresignedUrlRequest(bucketName, fileName).withMethod(HttpMethod.GET).withExpiration(expirationData);
       URL url= client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    @Override
    public String getImageUrlByName(String fileName) {
        S3Object object = client.getObject(bucketName,fileName);
        String key=object.getKey();
        String url=this.preSignedUrl(key);
        return url;
    }

    @Override
    public String deleteImageByName(String fileName) {
        try{
            client.deleteObject(bucketName, fileName);
            return "file deleted successfully";
        }catch (Exception e)
        {
            return e.getMessage();
        }

    }

}
