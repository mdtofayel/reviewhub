// src/main/java/com/reviewhub/api/mapper/ReviewMapper.java
package com.reviewhub.api.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewhub.api.entity.ReviewEntity;
import com.reviewhub.api.model.ReviewArticle;
import com.reviewhub.api.model.Review;
import com.reviewhub.api.model.Seo;

import java.time.Instant;
import java.util.*;

public class ReviewMapper {

    private final ObjectMapper objectMapper;

    public ReviewMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

 // src/main/java/com/reviewhub/api/mapper/ReviewMapper.java

    public ReviewArticle toModel(ReviewEntity entity) {
        Review reviewBody = null;
        Seo seo = null;
        Map<String, String> attributes = Collections.emptyMap();
        List<String> tags = Collections.emptyList();
        List<String> galleryImages = Collections.emptyList();

        try {
            if (entity.getReviewJson() != null) {
                reviewBody = objectMapper.readValue(entity.getReviewJson(), Review.class);
            }
            if (entity.getSeoJson() != null) {
                seo = objectMapper.readValue(entity.getSeoJson(), Seo.class);
            }
            if (entity.getAttributesJson() != null) {
                attributes = objectMapper.readValue(
                        entity.getAttributesJson(),
                        objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class)
                );
            }
            if (entity.getGalleryImagesJson() != null) {
                galleryImages = objectMapper.readValue(
                        entity.getGalleryImagesJson(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
                );
            }
        } catch (JsonProcessingException ignored) {}

        if (entity.getTags() != null && !entity.getTags().isEmpty()) {
            tags = Arrays.stream(entity.getTags().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }

        return new ReviewArticle(
                String.valueOf(entity.getId()),
                entity.getSlug(),
                entity.getTitle(),
                entity.getBrand(),
                entity.getPrice(),
                entity.getCurrency(),
                entity.getImageUrl(),
                entity.getRating(),
                entity.getReviewCount(),
                tags,
                attributes,
                reviewBody,
                seo,
                entity.getPublishedAt(),
                entity.getPrimaryCategory(),
                galleryImages
        );

    }

    public ReviewEntity toEntity(ReviewArticle model) {
        ReviewEntity entity = new ReviewEntity();

        if (model.id() != null) {
            try { entity.setId(Long.parseLong(model.id())); } catch (NumberFormatException ignored) {}
        }

        entity.setSlug(model.slug());
        entity.setTitle(model.title());
        entity.setBrand(model.brand());
        entity.setPrice(model.price());
        entity.setCurrency(model.currency());
        entity.setImageUrl(model.heroImageUrl());
        entity.setRating(model.rating());
        entity.setReviewCount(model.reviewCount());
        entity.setPrimaryCategory(model.primaryCategory());

        if (model.tags() != null) {
            entity.setTags(String.join(",", model.tags()));
        }

        try {
            if (model.attributes() != null) {
                entity.setAttributesJson(objectMapper.writeValueAsString(model.attributes()));
            }
            if (model.review() != null) {
                entity.setReviewJson(objectMapper.writeValueAsString(model.review()));
            }
            if (model.seo() != null) {
                entity.setSeoJson(objectMapper.writeValueAsString(model.seo()));
            }
            if (model.galleryImages() != null) {
                entity.setGalleryImagesJson(objectMapper.writeValueAsString(model.galleryImages()));
            }
        } catch (JsonProcessingException ignored) {}

        entity.setPublishedAt(model.publishedAt());

        return entity;
    }

}
