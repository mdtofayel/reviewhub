// src/main/java/com/reviewhub/api/controller/DealController.java
package com.reviewhub.api.controller;

import com.reviewhub.api.model.DealArticle;
import com.reviewhub.api.repo.InMemoryStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/deals")
public class DealController {

  private final InMemoryStore store;

  public DealController(InMemoryStore store) {
    this.store = store;
  }

  // featured deal for home hero
  @GetMapping("/featured")
  public ResponseEntity<DealArticle> featured() {
    // at the moment just return the first deal in the map
    return store.dealsBySlug.values().stream()
        .findFirst()
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // single deal page
  @GetMapping("/{slug}")
  public ResponseEntity<DealArticle> bySlug(@PathVariable("slug") String slug) {
    DealArticle deal = store.dealsBySlug.get(slug);
    if (deal == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(deal);
  }

  // optional list of all deals
  @GetMapping
  public ResponseEntity<List<DealArticle>> all() {
    return ResponseEntity.ok(new ArrayList<>(store.dealsBySlug.values()));
  }
}
