package com.reviewhub.api.controller;

import com.reviewhub.api.repo.InMemoryStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
  private final InMemoryStore store;
  public CategoryController(InMemoryStore store) { this.store = store; }

  @GetMapping
  public ResponseEntity<List<String>> all() {
    return ResponseEntity.ok(store.categories.stream().sorted().toList());
  }
}
