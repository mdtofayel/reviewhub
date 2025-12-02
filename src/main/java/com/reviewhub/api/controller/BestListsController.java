package com.reviewhub.api.controller;

import com.reviewhub.api.dto.BestListSummaryDto;
import com.reviewhub.api.dto.PostSummaryDto;
import com.reviewhub.api.model.Product;
import com.reviewhub.api.model.RoundupArticle;
import com.reviewhub.api.repo.InMemoryStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BestListsController {

  private final InMemoryStore store;

  public BestListsController(InMemoryStore store) {
    this.store = store;
  }

  @GetMapping("/best-lists")
  public List<BestListSummaryDto> bestLists() {
    AtomicInteger counter = new AtomicInteger(1);

    return store.roundupsBySlug
        .values()
        .stream()
        .sorted(Comparator.comparing(RoundupArticle::lastUpdatedAt).reversed())
        .map(ra -> {
          int id = counter.getAndIncrement();

          String heroImage = ra.heroImageUrl();
          if (heroImage == null || heroImage.isBlank()) {
            heroImage = ra.products().isEmpty()
                ? null
                : ra.products().get(0).image();
          }

          String summary = ra.introMd();
          if (summary != null && summary.length() > 160) {
            summary = summary.substring(0, 157) + "...";
          }

          return new BestListSummaryDto(
              id,
              ra.slug(),
              ra.title(),
              summary,
              heroImage,
              ra.category(),
              ra.author() != null ? ra.author() : "ReviewHub team",
              ra.lastUpdatedAt() != null ? ra.lastUpdatedAt() : ra.publishedAt()
          );
        })
        .collect(Collectors.toList());
  }

  @GetMapping("/posts/top")
  public List<PostSummaryDto> topPosts() {
    AtomicInteger counter = new AtomicInteger(1);

    return store.roundupsBySlug
        .values()
        .stream()
        .sorted(Comparator.comparing(RoundupArticle::lastUpdatedAt).reversed())
        .limit(4)
        .map(ra -> {
          String thumb = ra.heroImageUrl();
          if (thumb == null || thumb.isBlank()) {
            thumb = ra.products().isEmpty()
                ? null
                : ra.products().get(0).image();
          }

          Instant published = ra.lastUpdatedAt() != null ? ra.lastUpdatedAt() : ra.publishedAt();

          return new PostSummaryDto(
              counter.getAndIncrement(),
              ra.title(),
              "/roundups/" + ra.slug(),
              thumb,
              published
          );
        })
        .collect(Collectors.toList());
  }

  @GetMapping("/reviews/latest")
  public List<PostSummaryDto> latestReviews() {
    AtomicInteger counter = new AtomicInteger(1);

    return store.productsBySlug
        .values()
        .stream()
        .sorted(Comparator.comparing(Product::createdAt).reversed())
        .limit(4)
        .map(p -> {
          Instant published = p.createdAt() == null ? Instant.now() : p.createdAt();
          return new PostSummaryDto(
              counter.getAndIncrement(),
              p.title(),
              "/products/" + p.slug(),
              p.image(),
              published
          );
        })
        .collect(Collectors.toList());
  }
}
