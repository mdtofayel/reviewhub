package com.reviewhub.api.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reviewhub.api.dto.PagedResponse; 
import com.reviewhub.api.model.JobLog;
import com.reviewhub.api.model.ScrapeJob;
import com.reviewhub.api.service.JobService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/scrape-jobs")
public class JobController {
  private final JobService jobs;
  public JobController(JobService jobs) { this.jobs = jobs; }

  // POST /api/scrape-jobs  body { keyword, market?, depth? }
  @PostMapping
  public ResponseEntity<ScrapeJob> create(@RequestBody CreateJobBody body) {
    if (body.keyword == null || body.keyword.isBlank()) return ResponseEntity.badRequest().build();
    var job = jobs.create(body.keyword, body.market, body.depth);
    return ResponseEntity.ok(job);
  }
  public record CreateJobBody(String keyword, String market, Integer depth) {}

  // GET /api/scrape-jobs?page&size&status?
  @GetMapping
  public ResponseEntity<PagedResponse<ScrapeJob>> list(
      @RequestParam(name="page", defaultValue="0") @Min(0) int page,
      @RequestParam(name="size", defaultValue="10") @Min(1) @Max(100) int size,
      @RequestParam(name="status", required=false) String status
  ) {
    var items = jobs.list(page, size, status);
    var total = jobs.total(status);
    return ResponseEntity.ok(new PagedResponse<>(items, page, size, total));
  }


  // GET /api/scrape-jobs/{id}
  @GetMapping("/{id}")
  public ResponseEntity<ScrapeJob> byId(@PathVariable String id) {
    var job = jobs.byId(id);
    return job == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(job);
  }

  // GET /api/scrape-jobs/{id}/logs?after=ISO_INSTANT
  @GetMapping("/{id}/logs")
  public ResponseEntity<List<JobLog>> logs(
      @PathVariable String id,
      @RequestParam(required=false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Instant after
  ) {
    var exists = jobs.byId(id);
    if (exists == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(jobs.logs(id, after));
  }
}
