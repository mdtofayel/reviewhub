package com.reviewhub.api.controller;

import com.reviewhub.api.model.RoundupArticle;
import com.reviewhub.api.repo.InMemoryStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roundups")
public class RoundupController {
  private final InMemoryStore store;
  public RoundupController(InMemoryStore store) { this.store = store; }

  // GET /api/roundups/{slug}
  @GetMapping("/{slug}")
  public ResponseEntity<RoundupArticle> bySlug(@PathVariable String slug) {
    var a = store.roundupsBySlug.get(slug);
    return a == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(a);
  }
}
