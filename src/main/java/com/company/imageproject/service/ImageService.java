package com.company.imageproject.service;

import com.company.imageproject.service.dto.ImageDTO;
import com.company.imageproject.service.dto.ImageUploadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ImageService {
    ImageDTO storeAndSave(ImageUploadDTO imageUploadDTO);
    ImageDTO save(ImageDTO imageDTO);
    Page<ImageDTO> findAll(Pageable pageable);
    Page<ImageDTO> search(String query, Pageable pageable);
}
