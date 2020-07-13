package com.company.imageproject.infrastructure.blobstorage.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static com.company.imageproject.infrastructure.blobstorage.api.InputStreamUploadRequest.InputStreamUpload;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AWSS3Facade {

    private static final String S3_FILE_PATH_FORMAT = "%s/%s";
    private final TransferManager transferManager;

    String storeFile(FilePutObject filePutObject) throws AWSS3Exception {
        try {
            Upload upload = transferManager.upload(createPutObjectRequest(filePutObject));
            UploadResult uploadResult = upload.waitForUploadResult();
            return uploadResult.getKey();
        } catch (AmazonServiceException | InterruptedException e) {
            throw new AWSS3Exception("Failed to upload File(s)!", e);
        }
    }

    void deleteFile(String bucketName, String fileKey) throws AWSS3Exception {
        try {
            transferManager.getAmazonS3Client().deleteObject(bucketName, fileKey);
        } catch (AmazonServiceException e) {
            throw new AWSS3Exception("Failed to upload File(s)!", e);
        }
    }

    private PutObjectRequest createPutObjectRequest(FilePutObject filePutObject) {
        return new PutObjectRequest(
            filePutObject.getBucketName(), filePutObject.getFilePath(), filePutObject.getUploadedInputStream(),
            createBasicObjectMetadata(filePutObject.getMediaType(), filePutObject.getFileSize())
        ).withCannedAcl(CannedAccessControlList.PublicRead);
    }

    private ObjectMetadata createBasicObjectMetadata(String contentType, Long contentLength) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(contentLength);
        objectMetadata.setCacheControl("max-age=31536000, must-revalidate");
        return objectMetadata;
    }

    FilePutObject buildFilePutObjectData(InputStreamUpload file, String directoryName, String fileName, String bucketName, Instant createdDate) {
        return FilePutObject.builder()
                .uploadedInputStream(file.getInputStream())
                .fileSize(file.getSize())
                .filePath(generateFilePathForS3(directoryName, fileName))
                .mediaType(file.getContentType())
                .bucketName(bucketName)
                .createdDate(createdDate)
                .build();
    }

    private String generateFilePathForS3(String directoryName, String fileName) {
        return String.format(S3_FILE_PATH_FORMAT, directoryName, fileName);
    }

}
