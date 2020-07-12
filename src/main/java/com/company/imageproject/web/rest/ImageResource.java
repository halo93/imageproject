package com.company.imageproject.web.rest;

import com.company.imageproject.service.ImageService;
import com.company.imageproject.service.dto.ImageDTO;
import com.company.imageproject.service.dto.ImageUploadDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link com.company.imageproject.domain.Image}.
 */
@RestController
@RequestMapping("/api")
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "Image")
@Validated
@RequiredArgsConstructor
public class ImageResource {

    private static final String ENTITY_NAME = "image";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageService imageService;

    @ApiOperation("Upload an image")
    @PostMapping(path = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO> uploadAndSaveImage(
        @Valid @ModelAttribute ImageUploadDTO imageUploadDTO
    ) throws URISyntaxException {
        ImageDTO result = imageService.storeAndSave(imageUploadDTO);
        return ResponseEntity.created(new URI("/api/images/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @ApiOperation("Get all Images")
    @GetMapping("/images")
    public ResponseEntity<List<ImageDTO>> getAllImages(Pageable pageable) {
        Page<ImageDTO> page = imageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @ApiOperation("Search Images")
    @GetMapping("/_search/images")
    public ResponseEntity<List<ImageDTO>> searchImages(
        @ApiParam(name = "Query", example = "description:This is an image AND size:200", required = true)
        @RequestParam String query, Pageable pageable
    ) {
        Page<ImageDTO> page = imageService.search(query, pageable);
        if (page.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
