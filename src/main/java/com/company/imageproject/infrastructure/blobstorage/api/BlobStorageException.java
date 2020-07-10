package com.company.imageproject.infrastructure.blobstorage.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlobStorageException extends RuntimeException {

    private String errorKey;

    public BlobStorageException(String message) {
        super(message);
    }

    public BlobStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlobStorageException(String message, String errorKey) {
        this(message);
        this.errorKey = errorKey;
    }

    public BlobStorageException(String message, Throwable cause, String errorKey) {
        this(message, cause);
        this.errorKey = errorKey;
    }
}
