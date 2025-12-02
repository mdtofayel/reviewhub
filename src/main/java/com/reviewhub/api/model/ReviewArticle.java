// src/main/java/com/reviewhub/api/model/ReviewArticle.java
package com.reviewhub.api.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ReviewArticle(
        String id,
        String slug,
        String title,
        String brand,
        Double price,
        String currency,
        String heroImageUrl,
        Double rating,
        Integer reviewCount,
        List<String> tags,
        Map<String, String> attributes,
        Review review,
        Seo seo,
        Instant publishedAt,
        String primaryCategory
) {}
