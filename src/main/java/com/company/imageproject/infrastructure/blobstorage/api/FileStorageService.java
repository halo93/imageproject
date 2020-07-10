package com.company.imageproject.infrastructure.blobstorage.api;

public interface FileStorageService {
    InputStreamUploadResponse storeFile(InputStreamUploadRequest inputStreamToBeUploaded);
    void deleteFile(String fileKey);
}
