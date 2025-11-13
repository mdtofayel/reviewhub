package com.reviewhub.api.controller;

import com.reviewhub.api.model.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

  // very simple in memory store for now
  private final List<Review> reviews = new ArrayList<>();

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<Review> create(@RequestBody Review review) {
    // if your Review has updatedAt as String, set it when missing
    if (review.updatedAt() == null || review.updatedAt().isBlank()) {
      review = new Review(
        review.summary(),
        review.pros(),
        review.cons(),
        review.body_md(),
        review.author(),
        Instant.now().toString()
      );
    }
    reviews.add(review);
    return ResponseEntity.ok(review);
  }

  @GetMapping(produces = "application/json")
  public ResponseEntity<List<Review>> all() {
    return ResponseEntity.ok(reviews);
  }
}
