package com.reviewhub.api.service;

import com.reviewhub.api.model.DealArticle;
import com.reviewhub.api.repo.InMemoryStore;
import org.springframework.stereotype.Service;

@Service
public class DealService {

  private final InMemoryStore store;

  public DealService(InMemoryStore store) {
    this.store = store;
  }

  public DealArticle featured() {
    return store.dealsBySlug.values().stream().findFirst().orElse(null);
  }

  public DealArticle bySlug(String slug) {
    return store.dealsBySlug.get(slug);
  }
}
