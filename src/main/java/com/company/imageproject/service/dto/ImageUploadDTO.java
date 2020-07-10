package com.company.imageproject.service.dto;

import com.company.imageproject.service.validation.ImageFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ImageUploadDTO {

    @ApiModelProperty(
        value = "Uploaded image file",
        name = "uploadedImage",
        dataType = "MultipartFile",
        required = true
    )
    @NotNull
    @ImageFile
    private MultipartFile uploadedImage;

    @ApiModelProperty(
        value = "File Description",
        name = "description",
        dataType = "String",
        example = "This is the description of the file",
        required = true
    )
    @NotBlank
    @Size(max = 500)
    private String description;

}
