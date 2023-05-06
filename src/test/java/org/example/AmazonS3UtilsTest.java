package org.example;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import org.example.utils.S3Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class AmazonS3UtilsTest {

    private static final String BUCKET_NAME = "test-bucket";
    private S3Utils s3Utils;
    private AmazonS3 amazonS3;

    @BeforeEach
    public void setUp() {
        amazonS3 = Mockito.mock(AmazonS3.class);
        s3Utils = new S3Utils(amazonS3);
        ReflectionTestUtils.setField(s3Utils, "bucketName", BUCKET_NAME);
    }

    @Test
    public void testUploadFile() {
        byte[] mp3File = new byte[] { 1, 2, 3 };
        when(amazonS3.doesBucketExistV2(BUCKET_NAME)).thenReturn(true);

        String returnedObjectKey = s3Utils.uploadFile(mp3File);

        assertNotNull(returnedObjectKey);
        assertTrue(returnedObjectKey.endsWith(".mp3"));
        verify(amazonS3, times(1)).putObject(eq(BUCKET_NAME), anyString(), any(ByteArrayInputStream.class), any(ObjectMetadata.class));
    }

    @Test
    public void testDownloadFile() {
        String objectKey = UUID.randomUUID() + ".mp3";
        byte[] content = new byte[] { 1, 2, 3 };

        S3Object s3Object = mock(S3Object.class);
        S3ObjectInputStream objectContent = new S3ObjectInputStream(new ByteArrayInputStream(content), null);
        when(s3Object.getObjectContent()).thenReturn(objectContent);
        when(amazonS3.getObject(BUCKET_NAME, objectKey)).thenReturn(s3Object);

        byte[] result = s3Utils.downloadFile(objectKey);

        assertNotNull(result);
        assertArrayEquals(content, result);
    }

    @Test
    public void testDeleteFile() {
        String objectKey = UUID.randomUUID() + ".mp3";

        s3Utils.deleteFile(objectKey);

        verify(amazonS3, times(1)).deleteObject(BUCKET_NAME, objectKey);
    }
}
