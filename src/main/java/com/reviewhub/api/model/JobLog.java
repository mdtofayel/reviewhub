package com.reviewhub.api.model;

import java.time.Instant;

public record JobLog(
        Instant ts,
        String level,
        String message
) {}
