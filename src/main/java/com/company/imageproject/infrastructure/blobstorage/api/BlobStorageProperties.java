package com.company.imageproject.infrastructure.blobstorage.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.nio.file.Paths;

@Validated
@ConfigurationProperties(prefix = "company")
public class BlobStorageProperties {

    private static final FileStorage fileStorage = new FileStorage();

    public FileStorage getFileStorage() {
        return fileStorage;
    }

    @Getter
    @Setter
    public static class FileStorage {

        @NotEmpty
        private String bucketName = "changeme";

    }
}
