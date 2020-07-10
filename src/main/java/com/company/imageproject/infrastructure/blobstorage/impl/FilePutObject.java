package com.company.imageproject.infrastructure.blobstorage.impl;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.time.Instant;

@Builder
@Getter
public class FilePutObject {
    private InputStream uploadedInputStream;
    private Long fileSize;
    private String filePath;
    private String mediaType;
    private String bucketName;
    private Instant createdDate;
}
