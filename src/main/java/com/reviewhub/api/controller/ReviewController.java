// src/main/java/com/reviewhub/api/controller/ReviewController.java
package com.reviewhub.api.controller;

import com.reviewhub.api.model.ReviewArticle;
import com.reviewhub.api.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public Page<ReviewArticle> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return reviewService.search(q, category, page, size);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ReviewArticle> findOne(@PathVariable String slug) {
        Optional<ReviewArticle> review = reviewService.findBySlug(slug);
        return review
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReviewArticle> create(@RequestBody ReviewArticle article) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.create(article));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<ReviewArticle> update(
            @PathVariable String slug,
            @RequestBody ReviewArticle article
    ) {
        return reviewService.update(slug, article)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug) {
        boolean removed = reviewService.delete(slug);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
