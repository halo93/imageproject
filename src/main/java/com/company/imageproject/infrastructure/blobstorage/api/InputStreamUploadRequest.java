package com.company.imageproject.infrastructure.blobstorage.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InputStreamUploadRequest {

    @Valid
    InputStreamUpload uploadedInputStream;

    @NotBlank
    private String directoryName;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InputStreamUpload {

        @NotNull
        private InputStream inputStream;

        @NotBlank
        private String fileName;

        private String contentType;
        private long size;
        private Instant createdDate;

        public static InputStreamUpload of(MultipartFile multipartFile) {
            try {
                return InputStreamUpload.builder()
                        .inputStream(multipartFile.getInputStream())
                        .contentType(multipartFile.getContentType())
                        .fileName(multipartFile.getOriginalFilename())
                        .size(multipartFile.getSize())
                        .build();
            } catch (IOException e) {
                throw new BlobStorageException("Fail to get InputStream from request", e);
            }
        }
    }
}
