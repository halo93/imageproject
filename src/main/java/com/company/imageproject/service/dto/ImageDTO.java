package com.company.imageproject.service.dto;

import com.company.imageproject.infrastructure.blobstorage.api.InputStreamUploadResponse;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.company.imageproject.domain.Image} entity.
 */
public class ImageDTO implements Serializable {

    private Long id;

    @Size(max = 200)
    private String path;

    @Size(max = 500)
    @NotBlank
    private String description;

    @Size(max = 100)
    private String fileType;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "500000")
    private BigDecimal size;

    public static ImageDTO of(ImageUploadDTO imageUploadDTO, InputStreamUploadResponse inputStreamUploadResponse) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setPath(inputStreamUploadResponse.getPath());
        imageDTO.setDescription(imageUploadDTO.getDescription());
        imageDTO.setFileType(imageUploadDTO.getUploadedImage().getContentType());
        imageDTO.setSize(BigDecimal.valueOf(imageUploadDTO.getUploadedImage().getSize()));
        return imageDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageDTO)) {
            return false;
        }

        return id != null && id.equals(((ImageDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageDTO{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", size=" + getSize() +
            "}";
    }
}
