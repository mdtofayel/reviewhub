// src/main/java/com/reviewhub/api/service/ReviewService.java
package com.reviewhub.api.service;

import com.reviewhub.api.entity.ReviewEntity;
import com.reviewhub.api.mapper.ReviewMapper;
import com.reviewhub.api.model.ReviewArticle;
import com.reviewhub.api.repo.ReviewRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional(readOnly = true)
    public Page<ReviewArticle> search(String query, String category, int page, int size) {
        Pageable pageable =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));

        Page<ReviewEntity> pageData;

        if (category != null && !category.isBlank()) {
            pageData = reviewRepository.findByPrimaryCategoryIgnoreCase(category, pageable);
        } else if (query != null && !query.isBlank()) {
            pageData = reviewRepository.findByTitleContainingIgnoreCase(query, pageable);
        } else {
            pageData = reviewRepository.findAll(pageable);
        }

        return pageData.map(reviewMapper::toModel);
    }

    @Transactional(readOnly = true)
    public Optional<ReviewArticle> findBySlug(String slug) {
        return reviewRepository.findBySlug(slug).map(reviewMapper::toModel);
    }

    @Transactional
    public ReviewArticle create(ReviewArticle article) {
        ReviewEntity entity = reviewMapper.toEntity(article);
        ReviewEntity saved = reviewRepository.save(entity);
        return reviewMapper.toModel(saved);
    }

    @Transactional
    public Optional<ReviewArticle> update(String slug, ReviewArticle updated) {
        return reviewRepository.findBySlug(slug)
                .map(existing -> {
                    ReviewEntity entity = reviewMapper.toEntity(updated);
                    entity.setId(existing.getId());
                    ReviewEntity saved = reviewRepository.save(entity);
                    return reviewMapper.toModel(saved);
                });
    }

    @Transactional
    public boolean delete(String slug) {
        return reviewRepository.findBySlug(slug)
                .map(entity -> {
                    reviewRepository.delete(entity);
                    return true;
                })
                .orElse(false);
    }
}
