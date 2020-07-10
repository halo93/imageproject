package com.company.imageproject.web.rest;

import com.company.imageproject.ImageprojectApp;
import com.company.imageproject.domain.Image;
import com.company.imageproject.repository.ImageRepository;
import com.company.imageproject.repository.search.ImageSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ImageResource} REST controller.
 */
@SpringBootTest(classes = ImageprojectApp.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ImageResourceIT {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String SUPPORTED_FILE_TYPE_JPEG = "image/jpeg";
    private static final String SUPPORTED_FILE_TYPE_PNG = "image/png";
    private static final String UNSUPPORTED_FILE_TYPE = "image/gif";
    private static final BigDecimal DEFAULT_SIZE = new BigDecimal(1);

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageSearchRepository imageSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageMockMvc;

    private Image image;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createEntity(EntityManager em) {
        Image image = new Image()
            .path(DEFAULT_PATH)
            .description(DEFAULT_DESCRIPTION)
            .fileType(SUPPORTED_FILE_TYPE_JPEG)
            .size(DEFAULT_SIZE);
        return image;
    }

    @BeforeEach
    public void initTest() {
        image = createEntity(em);
    }

    @Test
    @Transactional
    public void shouldResponse201_WhenUploadImage_WithValidJpegFileAndDescription() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();
        // Create the Image
        final byte[] fileContent = "data".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.jpeg", SUPPORTED_FILE_TYPE_JPEG, fileContent);

        restImageMockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/images")
                .file(mockMultipartFile)
                .param("description", DEFAULT_DESCRIPTION)
        ).andExpect(status().isCreated());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeCreate + 1);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testImage.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_JPEG);
        assertThat(testImage.getSize()).isEqualTo(BigDecimal.valueOf(fileContent.length));
    }

    @Test
    @Transactional
    public void shouldResponse201_WhenUploadImage_WithValidPngFileAndDescription() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();
        // Create the Image
        final byte[] fileContent = "data".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.png", SUPPORTED_FILE_TYPE_PNG, fileContent);

        restImageMockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/images")
                .file(mockMultipartFile)
                .param("description", DEFAULT_DESCRIPTION)
        ).andExpect(status().isCreated());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeCreate + 1);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testImage.getFileType()).isEqualTo(SUPPORTED_FILE_TYPE_PNG);
        assertThat(testImage.getSize()).isEqualTo(BigDecimal.valueOf(fileContent.length));
    }

    @Test
    @Transactional
    public void shouldResponse400_WhenUploadImage_WithUnsupportedFile() throws Exception {
        int databaseSizeBeforeTest = imageRepository.findAll().size();

        // Create the Image
        final byte[] fileContent = "data".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.gif", UNSUPPORTED_FILE_TYPE, fileContent);

        restImageMockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/images")
                .file(mockMultipartFile)
                .param("description", DEFAULT_DESCRIPTION)
        ).andExpect(status().isBadRequest());

        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void shouldResponse400_WhenUploadImage_WithNoDescription() throws Exception {
        int databaseSizeBeforeTest = imageRepository.findAll().size();

        // Create the Image
        final byte[] fileContent = "data".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.png", SUPPORTED_FILE_TYPE_PNG, fileContent);

        restImageMockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/images")
                .file(mockMultipartFile)
        ).andExpect(status().isBadRequest());

        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void shouldResponse400_WhenUploadImage_WithExceededFileSize() throws Exception {
        // Create the Image
        final byte[] fileContent = new byte[500001];
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedImage", "original_filename.png", SUPPORTED_FILE_TYPE_PNG, fileContent);

        restImageMockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/images")
                .file(mockMultipartFile)
                .param("description", DEFAULT_DESCRIPTION)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void shouldResponse200_WhenGetAllImages() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList
        restImageMockMvc.perform(get("/api/images"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(SUPPORTED_FILE_TYPE_JPEG)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())));
    }

    @Test
    @Transactional
    public void shouldResponse200_WhenSearchImage_WithValidSearchQuery() throws Exception {
        // Initialize the database
        imageSearchRepository.save(image);

        // Search the image
        restImageMockMvc.perform(get("/api/_search/images?query=path:" + DEFAULT_PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(SUPPORTED_FILE_TYPE_JPEG)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())));
    }

    @Test
    @Transactional
    public void shouldResponse500_WhenSearchImage_WithInvalidSearchQuery() throws Exception {
        // Initialize the database
        imageSearchRepository.save(image);

        // Search the image
        restImageMockMvc.perform(get("/api/_search/images?query=:" + DEFAULT_PATH))
            .andExpect(status().isInternalServerError());

    }

    @Test
    @Transactional
    public void shouldResponse200WithEmptyResponse_WhenSearchImage_WithNotExistSearchQuery() throws Exception {
        // Initialize the database
        imageSearchRepository.save(image);

        // Search the image
        restImageMockMvc.perform(get("/api/_search/images?query=path:NOTEXIST"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").value(hasSize(0)));
    }

}
