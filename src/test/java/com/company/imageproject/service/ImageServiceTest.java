package com.company.imageproject.service;

import com.company.imageproject.domain.Image;
import com.company.imageproject.infrastructure.blobstorage.api.BlobStorageException;
import com.company.imageproject.infrastructure.blobstorage.api.FileStorageService;
import com.company.imageproject.infrastructure.blobstorage.api.InputStreamUploadRequest;
import com.company.imageproject.infrastructure.blobstorage.api.InputStreamUploadResponse;
import com.company.imageproject.repository.ImageRepository;
import com.company.imageproject.repository.search.ImageSearchRepository;
import com.company.imageproject.service.dto.ImageDTO;
import com.company.imageproject.service.dto.ImageUploadDTO;
import com.company.imageproject.service.mapper.ImageMapper;
import com.company.imageproject.service.mapper.ImageMapperImpl;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class ImageServiceTest {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String SUPPORTED_FILE_TYPE_JPEG = "image/jpeg";
    private static final String SUPPORTED_FILE_TYPE_PNG = "image/png";

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageSearchRepository imageSearchRepository;

    @Spy
    private ImageMapper imageMapper = new ImageMapperImpl();

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ImageServiceImpl testClass;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnImageDTO_WhenStoreAndSave_WithNoExceptionOccurred() {
        // ImageUploadDTO
        final byte[] fileContent = "data".getBytes();
        BigDecimal mockSize = BigDecimal.valueOf(fileContent.length);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.jpeg", SUPPORTED_FILE_TYPE_JPEG, fileContent);
        ImageUploadDTO mockImageUploadDTO = new ImageUploadDTO();
        mockImageUploadDTO.setUploadedImage(mockMultipartFile);
        mockImageUploadDTO.setDescription(DEFAULT_DESCRIPTION);

        // InputStreamUploadResponse
        Instant current = Instant.now();
        InputStreamUploadResponse mockInputStreamUploadResponse = InputStreamUploadResponse.of(DEFAULT_PATH, current);
        ImageDTO mockImageDTO = ImageDTO.of(mockImageUploadDTO, mockInputStreamUploadResponse);
        Image mockImage = imageMapper.toEntity(mockImageDTO);

        when(fileStorageService.storeFile(any(InputStreamUploadRequest.class))).thenReturn(mockInputStreamUploadResponse);
        when(imageRepository.save(any(Image.class))).thenAnswer(i -> {
            mockImage.setId(1L);
            return mockImage;
        });
        when(imageSearchRepository.save(any(Image.class))).thenReturn(mockImage);

        ImageDTO resultImageDTO = testClass.storeAndSave(mockImageUploadDTO);
        assertThat(resultImageDTO.getId()).isEqualTo(1L);
        assertThat(resultImageDTO.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(resultImageDTO.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(resultImageDTO.getSize()).isEqualTo(mockSize);
        assertThat(resultImageDTO.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        verify(fileStorageService, atLeastOnce()).storeFile(any(InputStreamUploadRequest.class));
        verify(imageRepository, atLeastOnce()).save(any(Image.class));
        verify(imageSearchRepository, atLeastOnce()).save(any(Image.class));
    }

    @Test
    public void shouldFail_WhenStoreAndSave_WithExceptionOccurredWhenStoringFile() {
        // ImageUploadDTO
        final byte[] fileContent = "data".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.jpeg", SUPPORTED_FILE_TYPE_JPEG, fileContent);
        ImageUploadDTO mockImageUploadDTO = new ImageUploadDTO();
        mockImageUploadDTO.setUploadedImage(mockMultipartFile);
        mockImageUploadDTO.setDescription(DEFAULT_DESCRIPTION);

        when(fileStorageService.storeFile(any(InputStreamUploadRequest.class))).thenThrow(new BlobStorageException("Failed to upload File!"));

        assertThrows(BlobStorageException.class, () -> testClass.storeAndSave(mockImageUploadDTO));

        verify(fileStorageService, atLeastOnce()).storeFile(any(InputStreamUploadRequest.class));
    }

    @Test
    public void shouldFail_WhenStoreAndSave_WithExceptionOccurredWhenSavingRecord() {
        // ImageUploadDTO
        final byte[] fileContent = "data".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.jpeg", SUPPORTED_FILE_TYPE_JPEG, fileContent);
        ImageUploadDTO mockImageUploadDTO = new ImageUploadDTO();
        mockImageUploadDTO.setUploadedImage(mockMultipartFile);
        mockImageUploadDTO.setDescription(DEFAULT_DESCRIPTION);

        // InputStreamUploadResponse
        Instant current = Instant.now();
        InputStreamUploadResponse mockInputStreamUploadResponse = InputStreamUploadResponse.of(DEFAULT_PATH, current);

        when(fileStorageService.storeFile(any(InputStreamUploadRequest.class))).thenReturn(mockInputStreamUploadResponse);
        when(imageRepository.save(any(Image.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> testClass.storeAndSave(mockImageUploadDTO));

        verify(fileStorageService, atLeastOnce()).storeFile(any(InputStreamUploadRequest.class));
        verify(imageRepository, atLeastOnce()).save(any(Image.class));
    }

    @Test
    public void shouldReturnImageDTOs_WhenGetListOfImages() {
        final byte[] fileContent = "data".getBytes();
        BigDecimal mockSize = BigDecimal.valueOf(fileContent.length);

        // Expected Response List<ImageDTO>
        Image mockImage = new Image();
        mockImage.setId(1L);
        mockImage.setPath(DEFAULT_PATH);
        mockImage.setSize(mockSize);
        mockImage.setFileType(SUPPORTED_FILE_TYPE_JPEG);
        mockImage.setDescription(DEFAULT_DESCRIPTION);

        Image mockImage2 = new Image();
        mockImage2.setId(2L);
        mockImage2.setPath(DEFAULT_PATH + "2");
        mockImage2.setSize(mockSize);
        mockImage2.setFileType(SUPPORTED_FILE_TYPE_PNG);
        mockImage2.setDescription(DEFAULT_DESCRIPTION);
        List<Image> mockImages = Arrays.asList(mockImage, mockImage2);

        PageRequest mockPageRequest = PageRequest.of(0, 20);
        when(imageRepository.findAll(mockPageRequest))
            .thenReturn(new PageImpl<>(mockImages, PageRequest.of(0, 2), 2));

        List<ImageDTO> resultImageDTOs = testClass.findAll(mockPageRequest).getContent();
        ImageDTO resultImageDTO1 = resultImageDTOs.get(0);
        ImageDTO resultImageDTO2 = resultImageDTOs.get(1);
        assertThat(resultImageDTOs.size()).isEqualTo(2L);
        assertThat(resultImageDTO1.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(resultImageDTO1.getSize()).isEqualTo(mockSize);
        assertThat(resultImageDTO1.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(resultImageDTO2.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(resultImageDTO2.getSize()).isEqualTo(mockSize);
        assertThat(resultImageDTO2.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_PNG);

        Mockito.verify(imageRepository, Mockito.atLeastOnce()).findAll(mockPageRequest);
    }

    @Test
    public void shouldReturnSearchedImages_WhenSearchImages_WithValidQuery() {
        final byte[] fileContent = "data".getBytes();
        BigDecimal mockSize = BigDecimal.valueOf(fileContent.length);

        // Expected Response List<ImageDTO>
        Image mockImage = new Image();
        mockImage.setId(1L);
        mockImage.setPath(DEFAULT_PATH);
        mockImage.setSize(mockSize);
        mockImage.setFileType(SUPPORTED_FILE_TYPE_JPEG);
        mockImage.setDescription(DEFAULT_DESCRIPTION);

        Image mockImage2 = new Image();
        mockImage2.setId(2L);
        mockImage2.setPath(DEFAULT_PATH + "2");
        mockImage2.setSize(mockSize);
        mockImage2.setFileType(SUPPORTED_FILE_TYPE_PNG);
        mockImage2.setDescription(DEFAULT_DESCRIPTION);
        List<Image> mockImages = Arrays.asList(mockImage, mockImage2);

        PageRequest mockPageRequest = PageRequest.of(0, 20);
        String queryString = String.format("description:%s AND size:%s", DEFAULT_DESCRIPTION, mockSize);
        QueryStringQueryBuilder query = queryStringQuery(queryString);
        when(imageSearchRepository.search(query, mockPageRequest))
            .thenReturn(new PageImpl<>(mockImages, PageRequest.of(0, 2), 2));

        List<ImageDTO> resultImageDTOs = testClass.search(queryString, mockPageRequest).getContent();
        ImageDTO resultImageDTO1 = resultImageDTOs.get(0);
        ImageDTO resultImageDTO2 = resultImageDTOs.get(1);
        assertThat(resultImageDTOs.size()).isEqualTo(2L);
        assertThat(resultImageDTO1.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(resultImageDTO1.getSize()).isEqualTo(mockSize);
        assertThat(resultImageDTO1.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(resultImageDTO2.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(resultImageDTO2.getSize()).isEqualTo(mockSize);
        assertThat(resultImageDTO2.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_PNG);

        Mockito.verify(imageSearchRepository, Mockito.atLeastOnce()).search(query, mockPageRequest);
    }

}
