package com.reviewhub.api.repo;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;


import com.reviewhub.api.model.JobLog;
import com.reviewhub.api.model.Product;
import com.reviewhub.api.model.Review;
import com.reviewhub.api.model.Faq;
import com.reviewhub.api.model.RoundupArticle;
import com.reviewhub.api.model.ScrapeJob;
import com.reviewhub.api.model.Seo;


import jakarta.annotation.PostConstruct;
@Component
public class InMemoryStore {

  public final Map<String, Product> productsBySlug = new ConcurrentHashMap<>();
  public final Set<String> categories = new HashSet<>();
  public final Map<String, RoundupArticle> roundupsBySlug = new ConcurrentHashMap<>();

  public final Map<String, ScrapeJob> jobsById = new ConcurrentHashMap<>();
  public final Map<String, List<JobLog>> logsByJobId = new ConcurrentHashMap<>();

  @PostConstruct
  public void seed() {
    // --- Categories ---
    categories.addAll(List.of("laptops","headphones","monitors","smartphones"));

    // --- Products ---
    for (int i = 1; i <= 30; i++) {
      String slug = "product-" + i;
      Product p = new Product(
          UUID.randomUUID().toString(),
          slug,
          "Sample Product " + i,
          (i % 2 == 0 ? "Acme" : "Contoso"),
          (i % 3 == 0 ? null : 99.99 + i),
          "EUR",
          "https://picsum.photos/seed/" + slug + "/800/600",
          3.5 + (i % 15) * 0.1,
          20 + i,
          List.of((i % 2 == 0) ? "Editor’s Choice" : "Great Value"),
          Map.of("Weight","1." + i + " kg","Color", (i%2==0?"Black":"Silver")),
          new Review(
              "Quick verdict for product " + i,
              new String[]{"Solid build","Good battery"},
              new String[]{"Average speakers"},
              "## Full Markdown Review\n\nThis is a _demo_ body for **product " + i + "**.",
              "Reviewer " + ((i%5)+1),
              Instant.now().toString()
          ),
          new Seo("Sample Product " + i, "SEO description for product " + i),
          Instant.now(),
          i%2==0 ? "laptops" : "headphones"
      );
      productsBySlug.put(slug, p);
    }

    // --- Roundup ---
 // --- Roundup ---
    RoundupArticle ra = new RoundupArticle(
        "best-budget-laptops-2025",
        "Best Budget Laptops (2025)",
        "Intro markdown...",
        productsBySlug.values().stream()
            .filter(p -> "laptops".equals(p.category()))
            .limit(5)
            .map(p -> new RoundupArticle.RoundupProduct(
                p.id(),
                p.slug(),
                p.title(),
                p.brand(),                 // brand
                p.price(),                 // price
                p.currency(),              // currency
                p.image(),                 // image
                p.rating() == null ? 0.0 : p.rating(),
                p.votes(),
                new Random().nextInt(10) + 1,   // rank
                "Short blurb",
                "Great for students"
            ))
            .sorted(Comparator.comparingInt(RoundupArticle.RoundupProduct::rank))
            .collect(Collectors.toList()),
        "Buying guide markdown...",
        List.of(new Faq("Is 8GB RAM enough?", "For light tasks, yes.")),
        "Conclusion markdown...",
        Instant.now(),  // updated_at is Instant
        new Seo("Best Budget Laptops 2025", "Top budget picks for 2025"),
        "laptops"
    );
    roundupsBySlug.put(ra.slug(), ra);

    roundupsBySlug.put(ra.slug(), ra);
  }
}
