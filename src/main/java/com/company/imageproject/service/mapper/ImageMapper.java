package com.company.imageproject.service.mapper;


import com.company.imageproject.domain.Image;
import com.company.imageproject.service.dto.ImageDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Image} and its DTO {@link ImageDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ImageMapper extends EntityMapper<ImageDTO, Image> {

    default Image fromId(Long id) {
        if (id == null) {
            return null;
        }
        Image image = new Image();
        image.setId(id);
        return image;
    }
}
