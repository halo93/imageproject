package com.company.imageproject.infrastructure.blobstorage.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.time.Instant;

import static com.company.imageproject.infrastructure.blobstorage.api.InputStreamUploadRequest.InputStreamUpload;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class AWSS3FacadeTest {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String SUPPORTED_FILE_TYPE_JPEG = "image/jpeg";
    private static final String BUCKET_NAME = "longimageprojectdummy";
    private static final String DEFAULT_FILE_KEY = "image/abc.jpeg";
    private static final String DEFAULT_BUCKET_NAME = "longimageprojectdummy";
    private static final String DEFAULT_FILE_NAME = "image.jpeg";

    @Mock
    private TransferManager transferManager;

    @InjectMocks
    private AWSS3Facade testClass;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnFileKey_WhenStoreFile_WithNoExceptionOccurred() throws Exception {
        Upload mockUpload = mock(Upload.class);
        final byte[] fileContent = "data".getBytes();
        ByteArrayInputStream mockInputStream = new ByteArrayInputStream(fileContent);
        long mockSize = "data".getBytes().length;
        Instant current = Instant.now();
        FilePutObject mockFilePutObject = FilePutObject.builder()
            .uploadedInputStream(mockInputStream)
            .uploadedInputStream(mockInputStream)
            .fileSize(mockSize)
            .mediaType(SUPPORTED_FILE_TYPE_JPEG)
            .bucketName(BUCKET_NAME)
            .createdDate(current)
            .build();
        UploadResult mockUploadResult = new UploadResult();
        mockUploadResult.setKey(DEFAULT_PATH);

        when(transferManager.upload(any(PutObjectRequest.class))).thenReturn(mockUpload);
        when(mockUpload.waitForUploadResult()).thenReturn(mockUploadResult);

        String resultFileKey = testClass.storeFile(mockFilePutObject);
        assertThat(resultFileKey).isEqualTo(DEFAULT_PATH);

        verify(transferManager, atLeastOnce()).upload(any(PutObjectRequest.class));
        verify(mockUpload, atLeastOnce()).waitForUploadResult();
    }

    @Test
    public void shouldFail_WhenStoreFile_WithExceptionOccurredInUploading() {
        final byte[] fileContent = "data".getBytes();
        ByteArrayInputStream mockInputStream = new ByteArrayInputStream(fileContent);
        long mockSize = "data".getBytes().length;
        Instant current = Instant.now();
        FilePutObject mockFilePutObject = FilePutObject.builder()
            .uploadedInputStream(mockInputStream)
            .uploadedInputStream(mockInputStream)
            .fileSize(mockSize)
            .mediaType(SUPPORTED_FILE_TYPE_JPEG)
            .bucketName(BUCKET_NAME)
            .createdDate(current)
            .build();

        when(transferManager.upload(any(PutObjectRequest.class))).thenThrow(new AmazonServiceException("Error"));

        assertThrows(AWSS3Exception.class, () -> testClass.storeFile(mockFilePutObject));

        verify(transferManager, atLeastOnce()).upload(any(PutObjectRequest.class));
    }

    @Test
    public void shouldFail_WhenStoreFile_WithExceptionOccurredInWaitingForUploadResult() throws Exception {
        Upload mockUpload = mock(Upload.class);
        final byte[] fileContent = "data".getBytes();
        ByteArrayInputStream mockInputStream = new ByteArrayInputStream(fileContent);
        long mockSize = "data".getBytes().length;
        Instant current = Instant.now();
        FilePutObject mockFilePutObject = FilePutObject.builder()
            .uploadedInputStream(mockInputStream)
            .uploadedInputStream(mockInputStream)
            .fileSize(mockSize)
            .mediaType(SUPPORTED_FILE_TYPE_JPEG)
            .bucketName(BUCKET_NAME)
            .createdDate(current)
            .build();

        when(transferManager.upload(any(PutObjectRequest.class))).thenReturn(mockUpload);
        when(mockUpload.waitForUploadResult()).thenThrow(new InterruptedException());

        assertThrows(AWSS3Exception.class, () -> testClass.storeFile(mockFilePutObject));

        verify(transferManager, atLeastOnce()).upload(any(PutObjectRequest.class));
        verify(mockUpload, atLeastOnce()).waitForUploadResult();
    }

    @Test
    public void shouldSuccess_WhenDeleteFile_WithNoExceptionOccurred() throws Exception {
        AmazonS3 mockAmazonS3 = mock(AmazonS3.class);

        when(transferManager.getAmazonS3Client()).thenReturn(mockAmazonS3);
        doNothing().when(mockAmazonS3).deleteObject(DEFAULT_BUCKET_NAME, DEFAULT_FILE_KEY);

        testClass.deleteFile(DEFAULT_BUCKET_NAME, DEFAULT_FILE_KEY);

        verify(transferManager, atLeastOnce()).getAmazonS3Client();
        verify(mockAmazonS3, atLeastOnce()).deleteObject(DEFAULT_BUCKET_NAME, DEFAULT_FILE_KEY);
    }

    @Test
    public void shouldFail_WhenDeleteFile_WithExceptionOccurred() {
        AmazonS3 mockAmazonS3 = mock(AmazonS3.class);

        when(transferManager.getAmazonS3Client()).thenReturn(mockAmazonS3);
        doThrow(AmazonServiceException.class).when(mockAmazonS3).deleteObject(DEFAULT_BUCKET_NAME, DEFAULT_FILE_KEY);

        assertThrows(AWSS3Exception.class, () -> testClass.deleteFile(DEFAULT_BUCKET_NAME, DEFAULT_FILE_KEY));

        verify(transferManager, atLeastOnce()).getAmazonS3Client();
        verify(mockAmazonS3, atLeastOnce()).deleteObject(DEFAULT_BUCKET_NAME, DEFAULT_FILE_KEY);
    }

    @Test
    public void shouldReturnFilePutObject_WhenBuildFilePutObject() throws Exception {
        final byte[] fileContent = "data".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", DEFAULT_FILE_NAME, SUPPORTED_FILE_TYPE_JPEG, fileContent);
        long mockSize = "data".getBytes().length;
        Instant current = Instant.now();

        FilePutObject resultFilePutObject = testClass.buildFilePutObjectData(
            InputStreamUpload.of(mockMultipartFile), DEFAULT_PATH, DEFAULT_FILE_NAME, DEFAULT_BUCKET_NAME, current
        );

        assertThat(resultFilePutObject.getMediaType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(resultFilePutObject.getBucketName()).isEqualTo(DEFAULT_BUCKET_NAME);
        assertThat(resultFilePutObject.getFileSize()).isEqualTo(mockSize);
        assertThat(resultFilePutObject.getCreatedDate()).isEqualTo(current);
        assertThat(resultFilePutObject.getFilePath()).isEqualTo(DEFAULT_PATH + "/" + DEFAULT_FILE_NAME);

    }
}
