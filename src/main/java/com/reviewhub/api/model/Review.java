package com.reviewhub.api.model;

public record Review(
    String summary,
    String[] pros,
    String[] cons,
    String body_md,
    String author,
    String updatedAt // ISO string for simplicity in seed; can be Instant later
) {}
