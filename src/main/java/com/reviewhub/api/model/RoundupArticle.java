// src/main/java/com/reviewhub/api/model/RoundupArticle.java
package com.reviewhub.api.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record RoundupArticle(
    String slug,
    String title,
    String subtitle,
    String heroImageUrl,
    String author,
    Instant publishedAt,
    Instant lastUpdatedAt,
    String introMd,
    List<RoundupProduct> products,
    String buyingGuideMd,
    String testingMethodMd,
    List<Faq> faqs,
    String conclusionMd,
    Seo seo,
    String category,
    List<TestMetricRow> testData,
    List<SpecRow> fullSpecs
) {
  public record RoundupProduct(
      String id,
      String slug,
      int rank,
      String title,
      String brand,
      Double price,
      String currency,
      String image,
      Double rating,
      Integer votes,
      String snippet,
      String verdict,
      List<String> pros,
      List<String> cons,
      Map<String, String> affiliateLinks,
      String reviewSlug
  ) {}
}
