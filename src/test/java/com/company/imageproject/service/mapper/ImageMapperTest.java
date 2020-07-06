package com.company.imageproject.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ImageMapperTest {

    private ImageMapper imageMapper;

    @BeforeEach
    public void setUp() {
        imageMapper = new ImageMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(imageMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(imageMapper.fromId(null)).isNull();
    }
}
