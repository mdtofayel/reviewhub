// src/main/java/com/reviewhub/api/repo/ReviewRepository.java
package com.reviewhub.api.repo;

import com.reviewhub.api.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findBySlug(String slug);

    Page<ReviewEntity> findByPrimaryCategoryIgnoreCase(String category, Pageable pageable);

    Page<ReviewEntity> findByTitleContainingIgnoreCase(String query, Pageable pageable);
}
