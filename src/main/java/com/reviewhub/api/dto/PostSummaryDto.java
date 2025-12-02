package com.reviewhub.api.dto;

import java.time.Instant;

public record PostSummaryDto(
    int id,
    String title,
    String url,
    String thumbnailUrl,
    Instant publishedAt
) {}
