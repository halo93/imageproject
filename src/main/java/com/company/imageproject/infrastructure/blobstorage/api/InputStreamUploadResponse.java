package com.company.imageproject.infrastructure.blobstorage.api;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.time.Instant;

@Builder
@Getter
public class InputStreamUploadResponse {

    @Size(max = 400)
    private String path;

    private Instant createdDate;

    public static InputStreamUploadResponse of(String path, Instant createdDate) {
        return InputStreamUploadResponse.builder()
            .path(path)
            .createdDate(createdDate)
            .build();
    }
}
