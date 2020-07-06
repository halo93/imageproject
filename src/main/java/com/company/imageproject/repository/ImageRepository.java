package com.company.imageproject.repository;

import com.company.imageproject.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Image entity.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
