package com.reviewhub.api.model;

import java.time.Instant;
import java.util.List;

public record RoundupArticle(
  String slug,
  String title,
  String intro_md,
  List<RoundupProduct> products,
  String buying_guide_md,
  List<Faq> faqs,          // uses top level Faq
  String conclusion_md,
  Instant updated_at,
  Seo seo,
  String category
) {
  public record RoundupProduct(
    String id,
    String slug,
    String title,
    String brand,
    Double price,
    String currency,
    String image,
    Double rating,
    Integer votes,
    int rank,
    String blurb,
    String verdict
  ) {}
}
