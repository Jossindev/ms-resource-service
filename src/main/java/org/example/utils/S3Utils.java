package org.example.utils;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Utils {

    @Value("${aws.bucket-name}")
    private String bucketName;
    private final AmazonS3 amazonS3;

    public String uploadFile(byte[] mp3File) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket(bucketName);
        }
        String objectKey = UUID.randomUUID() + ".mp3";
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(mp3File);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(mp3File.length);
            metadata.setContentType("application/octet-stream");
            amazonS3.putObject(bucketName, objectKey, byteStream, metadata);
            return objectKey;
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading the file to S3", e);
        }
    }

    public byte[] downloadFile(String objectKey) {
        try {
            return amazonS3.getObject(bucketName, objectKey)
                .getObjectContent()
                .readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading the file from S3", e);
        }
    }

    public void deleteFile(String objectKey) {
        try {
            amazonS3.deleteObject(bucketName, objectKey);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting the file from S3", e);
        }
    }
}
