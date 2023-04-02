package org.example.utils;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Utils {

    @Value("${aws.bucket-name}")
    private String bucketName;
    private final AmazonS3 s3Client;

    public String uploadFile(byte[] mp3File) {
        if (!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
        }
        String objectKey = UUID.randomUUID() + ".mp3";
        try {
            s3Client.putObject(bucketName, objectKey, Arrays.toString(mp3File));
            return objectKey;
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading the file to S3", e);
        }
    }

    public byte[] downloadFile(String objectKey) {
        System.out.println(s3Client.listBuckets());
        try {
            return s3Client.getObject(bucketName, objectKey)
                    .getObjectContent()
                    .readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading the file from S3", e);
        }
    }

    public void deleteFile(String objectKey) {
        try {
            s3Client.deleteObject(bucketName, objectKey);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting the file from S3", e);
        }
    }
}
