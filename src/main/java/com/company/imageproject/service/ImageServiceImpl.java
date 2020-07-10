package com.company.imageproject.service;

import com.company.imageproject.domain.Image;
import com.company.imageproject.infrastructure.blobstorage.api.FileStorageService;
import com.company.imageproject.infrastructure.blobstorage.api.InputStreamUploadRequest;
import com.company.imageproject.infrastructure.blobstorage.api.InputStreamUploadResponse;
import com.company.imageproject.repository.ImageRepository;
import com.company.imageproject.repository.search.ImageSearchRepository;
import com.company.imageproject.service.dto.ImageDTO;
import com.company.imageproject.service.dto.ImageUploadDTO;
import com.company.imageproject.service.mapper.ImageMapper;
import liquibase.util.file.FilenameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

import static com.company.imageproject.infrastructure.blobstorage.api.InputStreamUploadRequest.InputStreamUpload;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.mapstruct.ap.internal.util.JodaTimeConstants.DATE_TIME_FORMAT;

/**
 * Service Implementation for managing {@link Image}.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final String UPLOADED_FILE_NAME_FORMAT = "%s-%s.%s";
    private static final DateTimeFormatter FILE_NAME_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).withZone(ZoneOffset.UTC);
    private static final String MASTER_DIRECTORY_NAME = "images";

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final ImageSearchRepository imageSearchRepository;
    private final FileStorageService fileStorageService;

    @Override
    public ImageDTO storeAndSave(ImageUploadDTO imageUploadDTO) {
        InputStreamUploadResponse inputStreamUploadResponse = null;
        try {
            inputStreamUploadResponse = fileStorageService.storeFile(
                buildInputStreamUploadRequest(imageUploadDTO.getUploadedImage())
            );
            return save(ImageDTO.of(imageUploadDTO, inputStreamUploadResponse));
        } catch (Exception e) {
            if (Objects.nonNull(inputStreamUploadResponse)) {
                fileStorageService.deleteFile(inputStreamUploadResponse.getPath());
            }
            throw e;
        }

    }

    private InputStreamUploadRequest buildInputStreamUploadRequest(MultipartFile imageFile) {
        InputStreamUpload inputStreamUpload = InputStreamUpload.of(imageFile);
        inputStreamUpload.setFileName(generateFileNameToBeUploaded(imageFile.getOriginalFilename(), Instant.now()));
        return InputStreamUploadRequest.builder()
            .uploadedInputStream(inputStreamUpload)
            .directoryName(MASTER_DIRECTORY_NAME)
            .build();
    }

    private String generateFileNameToBeUploaded(String originalFileName, Instant createdDate) {
        return String.format(UPLOADED_FILE_NAME_FORMAT, UUID.randomUUID(),
            FILE_NAME_DATE_TIME_FORMAT.format(createdDate),
            FilenameUtils.getExtension(StringUtils.cleanPath(originalFileName))
        );
    }

    /**
     * Save a image.
     *
     * @param imageDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageDTO save(ImageDTO imageDTO) {
        Image image = imageMapper.toEntity(imageDTO);
        image = imageRepository.save(image);
        ImageDTO result = imageMapper.toDto(image);
        imageSearchRepository.save(image);
        return result;
    }

    /**
     * Get all the images.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageDTO> findAll(Pageable pageable) {
        return imageRepository.findAll(pageable)
            .map(imageMapper::toDto);
    }


    /**
     * Search for the image corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageDTO> search(String query, Pageable pageable) {
        return imageSearchRepository.search(queryStringQuery(query), pageable)
            .map(imageMapper::toDto);
    }
}
