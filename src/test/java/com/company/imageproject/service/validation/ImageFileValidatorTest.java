package com.company.imageproject.service.validation;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageFileValidatorTest {

    private static final String SUPPORTED_FILE_TYPE_JPEG = "image/jpeg";
    private static final String SUPPORTED_FILE_TYPE_PNG = "image/png";
    private static final String UNSUPPORTED_FILE_TYPE = "image/gif";

    private final ImageFileValidator validator = new ImageFileValidator();

    @Test
    void shouldBeValid_WhenValidateSupportedJpegImageFile() {
        // Create the Image
        final byte[] fileContent = "data".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.jpeg", SUPPORTED_FILE_TYPE_JPEG, fileContent);

        assertThat(isValid(mockMultipartFile)).isTrue();
    }

    @Test
    void shouldBeValid_WhenValidateSupportedPngImageFile() {
        // Create the Image
        final byte[] fileContent = "data".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.png", SUPPORTED_FILE_TYPE_PNG, fileContent);

        assertThat(isValid(mockMultipartFile)).isTrue();

    }

    @Test
    void shouldBeValid_WhenValidateUnsupportedImageFile() {
        // Create the Image
        final byte[] fileContent = "data".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.gif", UNSUPPORTED_FILE_TYPE, fileContent);

        assertThat(isValid(mockMultipartFile)).isFalse();
    }

    private boolean isValid(MultipartFile multipartFile) {
        return validator.isValid(multipartFile, null);
    }
}
