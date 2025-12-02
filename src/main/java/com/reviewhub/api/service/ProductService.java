package com.reviewhub.api.service;

import com.reviewhub.api.dto.PagedResponse;
import com.reviewhub.api.model.Product;
import com.reviewhub.api.repo.InMemoryStore;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class ProductService {
  private final InMemoryStore store;

  public ProductService(InMemoryStore store) { this.store = store; }

  public List<Product> random(int limit) {
	    return store.productsBySlug.values().stream()
	        .sorted(Comparator.comparing(Product::createdAt).reversed())
	        .limit(Math.max(1, limit))
	        .toList();
	  }


  public Product bySlug(String slug) {
    return store.productsBySlug.get(slug);
  }
  
  public PagedResponse<Product> search(
      String q, String category, String sort,
      Integer page, Integer size,
      Double minRating, Double priceMin, Double priceMax
  ) {
    int p = page == null ? 0 : Math.max(0, page);
    int s = size == null ? 12 : Math.max(1, Math.min(60, size));

    var stream = store.productsBySlug.values().stream();

    if (q != null && !q.isBlank()) {
      String needle = q.toLowerCase(Locale.ROOT);
      stream = stream.filter(pr ->
          pr.title().toLowerCase(Locale.ROOT).contains(needle) ||
          (pr.brand() != null && pr.brand().toLowerCase(Locale.ROOT).contains(needle))
      );
    }
    if (category != null && !category.isBlank()) {
      stream = stream.filter(pr -> Objects.equals(pr.category(), category));
    }
    if (minRating != null) {
      stream = stream.filter(pr -> pr.rating() != null && pr.rating() >= minRating);
    }
    if (priceMin != null) {
      stream = stream.filter(pr -> pr.price() != null && pr.price() >= priceMin);
    }
    if (priceMax != null) {
      stream = stream.filter(pr -> pr.price() != null && pr.price() <= priceMax);
    }

    Comparator<Product> cmp = switch (sort == null ? "relevance" : sort) {
      case "rating_desc" -> Comparator.comparing(Product::rating, Comparator.nullsLast(Double::compareTo)).reversed();
      case "newest" -> Comparator.comparing(Product::createdAt, Comparator.nullsLast((a,b) -> a.compareTo(b))).reversed();
      case "price_asc" -> Comparator.comparing(Product::price, Comparator.nullsLast(Double::compareTo));
      case "price_desc" -> Comparator.comparing(Product::price, Comparator.nullsLast(Double::compareTo)).reversed();
      default -> Comparator.comparing(Product::title); // naive "relevance"
    };

    var filtered = stream.sorted(cmp).toList();
    long total = filtered.size();
    int from = Math.min(p * s, filtered.size());
    int to = Math.min(from + s, filtered.size());
    var pageItems = filtered.subList(from, to);

    return new PagedResponse<>(pageItems, p, s, total);
  }
}
