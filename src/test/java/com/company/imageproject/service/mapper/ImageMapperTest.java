package com.company.imageproject.service.mapper;

import com.company.imageproject.domain.Image;
import com.company.imageproject.service.dto.ImageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageMapperTest {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String SUPPORTED_FILE_TYPE_JPEG = "image/jpeg";
    private static final String SUPPORTED_FILE_TYPE_PNG = "image/png";
    private static final BigDecimal DEFAULT_SIZE = new BigDecimal(1);

    private ImageMapper imageMapper;

    @BeforeEach
    public void setUp() {
        imageMapper = new ImageMapperImpl();
    }

    @Test
    public void shouldReturnEntity_WhenMapFromId() {
        Long id = 1L;
        assertThat(imageMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(imageMapper.fromId(null)).isNull();
    }

    @Test
    public void shouldReturnEntity_WhenMapFromDTO() {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(1L);
        imageDTO.setPath(DEFAULT_PATH);
        imageDTO.setDescription(DEFAULT_DESCRIPTION);
        imageDTO.setFileType(SUPPORTED_FILE_TYPE_JPEG);
        imageDTO.setSize(DEFAULT_SIZE);

        Image mappedImage = imageMapper.toEntity(imageDTO);

        assertThat(mappedImage.getId()).isEqualTo(1L);
        assertThat(mappedImage.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(mappedImage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(mappedImage.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(mappedImage.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    public void shouldReturnNull_WhenMapFromNullDTO() {
        Image mappedImage = imageMapper.toEntity((ImageDTO) null);
        assertThat(mappedImage).isNull();
    }

    @Test
    public void shouldReturnEntities_WhenMapFromDTOs() {
        ImageDTO imageDTO1 = new ImageDTO();
        imageDTO1.setId(1L);
        imageDTO1.setPath(DEFAULT_PATH);
        imageDTO1.setDescription(DEFAULT_DESCRIPTION);
        imageDTO1.setFileType(SUPPORTED_FILE_TYPE_JPEG);
        imageDTO1.setSize(DEFAULT_SIZE);

        ImageDTO imageDTO2 = new ImageDTO();
        imageDTO2.setId(2L);
        imageDTO2.setPath(DEFAULT_PATH);
        imageDTO2.setDescription(DEFAULT_DESCRIPTION);
        imageDTO2.setFileType(SUPPORTED_FILE_TYPE_PNG);
        imageDTO2.setSize(DEFAULT_SIZE);

        List<Image> mappedImages = imageMapper.toEntity(Arrays.asList(imageDTO1, imageDTO2));
        Image image1 = mappedImages.get(0);
        Image image2 = mappedImages.get(1);

        assertThat(mappedImages.size()).isEqualTo(2);

        assertThat(image1.getId()).isEqualTo(1L);
        assertThat(image1.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(image1.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(image1.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(image1.getSize()).isEqualTo(DEFAULT_SIZE);

        assertThat(image2.getId()).isEqualTo(2L);
        assertThat(image2.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(image2.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(image2.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_PNG);
        assertThat(image2.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    public void shouldReturnNull_WhenMapFromNullDTOList() {
        List<Image> mappedImages = imageMapper.toEntity((List<ImageDTO>) null);
        assertThat(mappedImages).isNull();
    }

    @Test
    public void shouldReturnDTO_WhenMapFromEntity() {
        Image image = new Image();
        image.setId(1L);
        image.setPath(DEFAULT_PATH);
        image.setDescription(DEFAULT_DESCRIPTION);
        image.setFileType(SUPPORTED_FILE_TYPE_JPEG);
        image.setSize(DEFAULT_SIZE);

        ImageDTO mappedImageDTO = imageMapper.toDto(image);

        assertThat(mappedImageDTO.getId()).isEqualTo(1L);
        assertThat(mappedImageDTO.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(mappedImageDTO.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(mappedImageDTO.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(mappedImageDTO.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    public void shouldReturnNull_WhenMapFromNullEntity() {
        ImageDTO mappedImageDTO = imageMapper.toDto((Image) null);
        assertThat(mappedImageDTO).isNull();
    }

    @Test
    public void shouldReturnDTOs_WhenMapFromEntities() {
        Image image1 = new Image();
        image1.setId(1L);
        image1.setPath(DEFAULT_PATH);
        image1.setDescription(DEFAULT_DESCRIPTION);
        image1.setFileType(SUPPORTED_FILE_TYPE_JPEG);
        image1.setSize(DEFAULT_SIZE);

        Image image2 = new Image();
        image2.setId(2L);
        image2.setPath(DEFAULT_PATH);
        image2.setDescription(DEFAULT_DESCRIPTION);
        image2.setFileType(SUPPORTED_FILE_TYPE_PNG);
        image2.setSize(DEFAULT_SIZE);

        List<ImageDTO> mappedImageDTOs = imageMapper.toDto(Arrays.asList(image1, image2));
        ImageDTO imageDTO1 = mappedImageDTOs.get(0);
        ImageDTO imageDTO2 = mappedImageDTOs.get(1);

        assertThat(mappedImageDTOs.size()).isEqualTo(2);

        assertThat(imageDTO1.getId()).isEqualTo(1L);
        assertThat(imageDTO1.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(imageDTO1.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(imageDTO1.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(imageDTO1.getSize()).isEqualTo(DEFAULT_SIZE);

        assertThat(imageDTO2.getId()).isEqualTo(2L);
        assertThat(imageDTO2.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(imageDTO2.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(imageDTO2.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_PNG);
        assertThat(imageDTO2.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    public void shouldReturnNull_WhenMapFromNullEntityList() {
        List<ImageDTO> mappedImageDTOs = imageMapper.toDto((List<Image>) null);
        assertThat(mappedImageDTOs).isNull();
    }
}
