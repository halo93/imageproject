package com.company.imageproject.domain;


import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * A Image.
 */
@Entity
@Table(name = "images")
@Document(indexName = "image")
public class Image extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 200)
    @Column(name = "path", length = 200)
    private String path;

    @Size(max = 500)
    @Column(name = "picture_description", length = 500)
    private String pictureDescription;

    @Size(max = 100)
    @Column(name = "file_type", length = 100)
    private String fileType;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "500000")
    @Column(name = "size", precision = 21, scale = 2, nullable = false)
    private BigDecimal size;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public Image path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPictureDescription() {
        return pictureDescription;
    }

    public Image pictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
        return this;
    }

    public void setPictureDescription(String pictureDescription) {
        this.pictureDescription = pictureDescription;
    }

    public String getFileType() {
        return fileType;
    }

    public Image fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public BigDecimal getSize() {
        return size;
    }

    public Image size(BigDecimal size) {
        this.size = size;
        return this;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Image)) {
            return false;
        }
        return id != null && id.equals(((Image) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Image{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", pictureDescription='" + getPictureDescription() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", size=" + getSize() +
            "}";
    }
}
