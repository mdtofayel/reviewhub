package com.reviewhub.api.model;

import java.time.Instant;

public record ScrapeJob(
        String id,
        String keyword,
        String market,
        Integer depth,
        String searchUrl,
        String status,
        Instant createdAt,
        Instant startedAt,
        Instant endedAt,
        String error
) {}
