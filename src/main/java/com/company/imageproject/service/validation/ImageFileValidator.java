package com.company.imageproject.service.validation;

import com.company.imageproject.domain.enumeration.SupportedImageType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile imageFile, ConstraintValidatorContext context) {
        return Objects.isNull(imageFile) || SupportedImageType.getAllSupportedImageTypes().stream()
            .anyMatch(e -> e.getMimeType().equalsIgnoreCase(imageFile.getContentType()));
    }
}
