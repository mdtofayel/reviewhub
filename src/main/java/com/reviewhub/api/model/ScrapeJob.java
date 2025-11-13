package com.reviewhub.api.model;

import java.time.Instant;

public record ScrapeJob(
    String id,
    String keyword,
    String market,
    Integer depth,
    String status,    // PENDING|RUNNING|SUCCEEDED|FAILED
    Instant createdAt,
    Instant startedAt,
    Instant endedAt,
    String errorMsg
) {}
