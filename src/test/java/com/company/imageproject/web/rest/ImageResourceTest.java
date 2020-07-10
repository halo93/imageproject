package com.company.imageproject.web.rest;

import com.company.imageproject.service.ImageService;
import com.company.imageproject.service.dto.ImageDTO;
import com.company.imageproject.service.dto.ImageUploadDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageResourceTest {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String SUPPORTED_FILE_TYPE_JPEG = "image/jpeg";
    private static final String SUPPORTED_FILE_TYPE_PNG = "image/png";

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageResource testClass;

    @Test
    void shouldReturnCreatedResponseEntity_WhenUploadImage_WithValidUploadDTO() throws Exception {
        // ImageUploadDTO
        final byte[] fileContent = "data".getBytes();
        BigDecimal mockSize = BigDecimal.valueOf(fileContent.length);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.jpeg", SUPPORTED_FILE_TYPE_JPEG, fileContent);
        ImageUploadDTO mockImageUploadDTO = new ImageUploadDTO();
        mockImageUploadDTO.setUploadedImage(mockMultipartFile);
        mockImageUploadDTO.setDescription(DEFAULT_DESCRIPTION);

        // Expected Response ImageDTO
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(1L);
        imageDTO.setPath(DEFAULT_PATH);
        imageDTO.setSize(mockSize);
        imageDTO.setFileType(SUPPORTED_FILE_TYPE_JPEG);
        imageDTO.setDescription(DEFAULT_DESCRIPTION);

        when(imageService.storeAndSave(any(ImageUploadDTO.class))).thenReturn(imageDTO);

        ResponseEntity<ImageDTO> resultResponseEntity = testClass.uploadAndSaveImage(mockImageUploadDTO);
        assertThat(resultResponseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(resultResponseEntity.getHeaders().getLocation().getPath()).isEqualTo("/api/images/");
        assertThat(resultResponseEntity.getBody().getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(resultResponseEntity.getBody().getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(resultResponseEntity.getBody().getSize()).isEqualTo(mockSize);

        Mockito.verify(imageService, Mockito.atLeastOnce()).storeAndSave(mockImageUploadDTO);
    }

    @Test
    void shouldReturnListImageResponseEntity_WhenGetAllImages() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        final byte[] fileContent = "data".getBytes();
        BigDecimal mockSize = BigDecimal.valueOf(fileContent.length);

        // Expected Response List<ImageDTO>
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(1L);
        imageDTO.setPath(DEFAULT_PATH);
        imageDTO.setSize(mockSize);
        imageDTO.setFileType(SUPPORTED_FILE_TYPE_JPEG);
        imageDTO.setDescription(DEFAULT_DESCRIPTION);
        List<ImageDTO> imageDTOs = Collections.singletonList(imageDTO);

        PageRequest mockPageRequest = PageRequest.of(0, 20);
        when(imageService.findAll(mockPageRequest))
            .thenReturn(new PageImpl<>(imageDTOs, PageRequest.of(0, 1), 1));

        ResponseEntity<List<ImageDTO>> resultResponseEntity = testClass.getAllImages(mockPageRequest);
        ImageDTO respondedImageDTO = resultResponseEntity.getBody().get(0);
        assertThat(resultResponseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(resultResponseEntity.getHeaders().getFirst("X-Total-Count")).isEqualTo("1");
        assertThat(respondedImageDTO.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(respondedImageDTO.getSize()).isEqualTo(mockSize);
        assertThat(respondedImageDTO.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);

        Mockito.verify(imageService, Mockito.atLeastOnce()).findAll(mockPageRequest);
    }

    @Test
    void shouldReturnSearchedImagesResponseEntity_WhenSearchImages() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        final byte[] fileContent = "data".getBytes();
        BigDecimal mockSize = BigDecimal.valueOf(fileContent.length);

        // Expected Response List<ImageDTO>
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(1L);
        imageDTO.setPath(DEFAULT_PATH);
        imageDTO.setSize(mockSize);
        imageDTO.setFileType(SUPPORTED_FILE_TYPE_JPEG);
        imageDTO.setDescription(DEFAULT_DESCRIPTION);

        ImageDTO imageDTO2 = new ImageDTO();
        imageDTO2.setId(2L);
        imageDTO2.setPath(DEFAULT_PATH + "2");
        imageDTO2.setSize(mockSize);
        imageDTO2.setFileType(SUPPORTED_FILE_TYPE_PNG);
        imageDTO2.setDescription(DEFAULT_DESCRIPTION);
        List<ImageDTO> imageDTOs = Arrays.asList(imageDTO, imageDTO2);

        PageRequest mockPageRequest = PageRequest.of(0, 20);
        String queryString = String.format("description:%s AND size:%s", DEFAULT_DESCRIPTION, mockSize);
        when(imageService.search(queryString, mockPageRequest))
            .thenReturn(new PageImpl<>(imageDTOs, PageRequest.of(0, 2), 2));

        ResponseEntity<List<ImageDTO>> resultResponseEntity = testClass.searchImages(queryString, mockPageRequest);
        ImageDTO respondedImageDTO1 = resultResponseEntity.getBody().get(0);
        ImageDTO respondedImageDTO2 = resultResponseEntity.getBody().get(1);
        assertThat(resultResponseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(resultResponseEntity.getHeaders().getFirst("X-Total-Count")).isEqualTo("2");
        assertThat(respondedImageDTO1.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(respondedImageDTO1.getSize()).isEqualTo(mockSize);
        assertThat(respondedImageDTO1.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(respondedImageDTO2.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(respondedImageDTO2.getSize()).isEqualTo(mockSize);
        assertThat(respondedImageDTO2.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_PNG);

        Mockito.verify(imageService, Mockito.atLeastOnce()).search(queryString, mockPageRequest);
    }


}
