package com.company.imageproject.service;

import com.company.imageproject.domain.Image;
import com.company.imageproject.repository.ImageRepository;
import com.company.imageproject.repository.search.ImageSearchRepository;
import com.company.imageproject.service.dto.ImageDTO;
import com.company.imageproject.service.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Image}.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final ImageSearchRepository imageSearchRepository;

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
     * Get one image by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImageDTO> findOne(Long id) {
        return imageRepository.findById(id)
            .map(imageMapper::toDto);
    }

    /**
     * Delete the image by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        imageRepository.deleteById(id);
        imageSearchRepository.deleteById(id);
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
