package com.reviewhub.api.controller;

import com.reviewhub.api.dto.PagedResponse;
import com.reviewhub.api.model.Product;
import com.reviewhub.api.service.ProductService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService svc;
  public ProductController(ProductService svc) { this.svc = svc; }

  @GetMapping("/random")
  public ResponseEntity<List<Product>> random(
      @RequestParam(name = "limit", defaultValue = "12") @Min(1) @Max(50) int limit) {
    return ResponseEntity.ok(svc.random(limit));
  }

  @GetMapping("/{slug}")
  public ResponseEntity<Product> bySlug(@PathVariable("slug") String slug) {
    var p = svc.bySlug(slug);
    return p == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
  }

  @GetMapping
  public ResponseEntity<PagedResponse<Product>> search(
      @RequestParam(name = "q", required = false) String q,
      @RequestParam(name = "category", required = false) String category,
      @RequestParam(name = "sort", defaultValue = "relevance") String sort,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "12") int size,
      @RequestParam(name = "minRating", required = false) Double minRating,
      @RequestParam(name = "priceMin", required = false) Double priceMin,
      @RequestParam(name = "priceMax", required = false) Double priceMax
  ) {
    return ResponseEntity.ok(svc.search(q, category, sort, page, size, minRating, priceMin, priceMax));
  }

}
