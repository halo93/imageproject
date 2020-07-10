package com.company.imageproject.domain.enumeration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum SupportedImageType {

    JPEG("image/jpeg"),
    PNG("image/png");

    private final String mimeType;

    public static List<SupportedImageType> getAllSupportedImageTypes() {
        return Arrays.stream(SupportedImageType.values()).collect(Collectors.toList());
    }
}
