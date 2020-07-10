package com.company.imageproject.infrastructure.blobstorage.impl;

class AWSS3Exception extends Exception {

    AWSS3Exception(String message) {
        super(message);
    }

    AWSS3Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
