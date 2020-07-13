package com.company.imageproject.infrastructure.blobstorage.impl;

import com.company.imageproject.infrastructure.blobstorage.api.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Service
class AWSFileStorageService implements FileStorageService {

    private final BlobStorageProperties blobStorageProperties;
    private final AWSS3Facade s3Service;

    @Override
    public InputStreamUploadResponse storeFile(InputStreamUploadRequest inputStreamToBeUploaded) {
        FilePutObject filePutObject = createFilePutObjectForStoring(inputStreamToBeUploaded.getDirectoryName(), inputStreamToBeUploaded.getUploadedInputStream());
        return storeOnCloudAndBuildResponse(filePutObject);
    }

    @Override
    public void deleteFile(String fileKey) {
        try {
            s3Service.deleteFile(blobStorageProperties.getFileStorage().getBucketName(), fileKey);
        } catch (AWSS3Exception e) {
            throw new BlobStorageException("Failed to remove File !", e);
        }
    }

    private FilePutObject createFilePutObjectForStoring(String directoryName, InputStreamUploadRequest.InputStreamUpload file) {
        return s3Service.buildFilePutObjectData(file, directoryName,
                file.getFileName(),
                blobStorageProperties.getFileStorage().getBucketName(),
                file.getCreatedDate()
        );
    }

    private InputStreamUploadResponse storeOnCloudAndBuildResponse(FilePutObject filePutObject) {
        try {
            String fileKey = s3Service.storeFile(filePutObject);
            return InputStreamUploadResponse.of(fileKey, filePutObject.getCreatedDate());
        } catch (AWSS3Exception e) {
            throw new BlobStorageException("Failed to upload File!", e);
        }
    }

}
