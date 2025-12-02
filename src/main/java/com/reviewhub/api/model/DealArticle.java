// src/main/java/com/reviewhub/api/model/DealArticle.java
package com.reviewhub.api.model;

import java.time.Instant;
import java.util.List;          // <-- very important

public record DealArticle(
    String slug,
    String title,
    String label,
    String heroImageUrl,
    String author,
    Instant publishedAt,
    String introMd,
    String bodyMd,
    Seo seo,

    // new fields
    String dealsBlockTitle,
    List<DealProduct> dealProducts
) {}
