package com.reviewhub.api.dto;

public record NavCategoryCreateRequest(
        String name,
        String slug,
        String path,
        Integer sortOrder,
        Long parentId,
        Boolean visible
) {
}