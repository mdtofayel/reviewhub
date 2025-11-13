package com.reviewhub.api.service;

import com.reviewhub.api.model.JobLog;
import com.reviewhub.api.model.ScrapeJob;
import com.reviewhub.api.repo.InMemoryStore;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {
  private final InMemoryStore store;

  public JobService(InMemoryStore store) { this.store = store; }

  public ScrapeJob create(String keyword, String market, Integer depth) {
    String id = UUID.randomUUID().toString();
    var job = new ScrapeJob(
        id, keyword, market, depth == null ? 10 : depth,
        "RUNNING", Instant.now(), Instant.now(), null, null
    );
    store.jobsById.put(id, job);
    store.logsByJobId.put(id, new ArrayList<>(List.of(
        new JobLog(Instant.now(), "INFO", "Job created: " + keyword),
        new JobLog(Instant.now(), "INFO", "Fetching SERP ..."),
        new JobLog(Instant.now(), "INFO", "Scraping top results ...")
    )));
    // Simulate finish
    store.logsByJobId.get(id).add(new JobLog(Instant.now(), "INFO", "Done."));
    store.jobsById.put(id, new ScrapeJob(
        id, keyword, market, job.depth(), "SUCCEEDED",
        job.createdAt(), job.startedAt(), Instant.now(), null
    ));
    return store.jobsById.get(id);
  }

  public List<ScrapeJob> list(int page, int size, String status) {
    var stream = store.jobsById.values().stream();
    if (status != null && !status.isBlank()) {
      stream = stream.filter(j -> status.equalsIgnoreCase(j.status()));
    }
    var sorted = stream
        .sorted(Comparator.comparing(ScrapeJob::createdAt).reversed())
        .toList();
    int from = Math.min(page * size, sorted.size());
    int to = Math.min(from + size, sorted.size());
    return sorted.subList(from, to);
  }

  public long total(String status) {
    return store.jobsById.values().stream()
        .filter(j -> status == null || status.equalsIgnoreCase(j.status()))
        .count();
  }

  public ScrapeJob byId(String id) { return store.jobsById.get(id); }

  public List<JobLog> logs(String id, Instant after) {
    var logs = store.logsByJobId.getOrDefault(id, List.of());
    if (after == null) return logs;
    return logs.stream().filter(l -> l.ts().isAfter(after)).collect(Collectors.toList());
  }
}
