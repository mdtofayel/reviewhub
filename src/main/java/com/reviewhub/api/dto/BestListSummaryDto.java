package com.reviewhub.api.dto;

import java.time.Instant;

public record BestListSummaryDto(
    int id,
    String slug,
    String title,
    String summary,
    String heroImageUrl,
    String label,
    String author,
    Instant publishedAt
) {}
