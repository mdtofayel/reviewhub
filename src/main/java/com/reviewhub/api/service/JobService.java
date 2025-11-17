package com.reviewhub.api.service;

import com.reviewhub.api.dto.RawProductRequest;
import com.reviewhub.api.model.JobLogEntity;
import com.reviewhub.api.model.RawProduct;
import com.reviewhub.api.model.ScrapeJobEntity;
import com.reviewhub.api.model.JobLog;
import com.reviewhub.api.model.ScrapeJob;
import com.reviewhub.api.repo.JobLogRepository;
import com.reviewhub.api.repo.RawProductRepository;
import com.reviewhub.api.repo.ScrapeJobRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {

    private final ScrapeJobRepository jobs;
    private final JobLogRepository logs;
    private final RawProductRepository rawProducts;
    private final ScraperLauncherService scraperLauncher;

    public JobService(
            ScrapeJobRepository jobs,
            JobLogRepository logs,
            RawProductRepository rawProducts,
            ScraperLauncherService scraperLauncher
    ) {
        this.jobs = jobs;
        this.logs = logs;
        this.rawProducts = rawProducts;
        this.scraperLauncher = scraperLauncher;
    }

    public ScrapeJob create(String keyword, String market, Integer depth, String searchUrl) {
        String id = UUID.randomUUID().toString();

        ScrapeJobEntity entity = new ScrapeJobEntity();
        entity.setId(id);
        entity.setKeyword(keyword);
        entity.setMarket(market);
        entity.setDepth(depth == null ? 10 : depth);
        entity.setSearchUrl(searchUrl);
        entity.setStatus("PENDING");
        entity.setCreatedAt(Instant.now());
        entity.setStartedAt(null);
        entity.setEndedAt(null);
        entity.setError(null);

        jobs.save(entity);

        addLog(entity, "INFO", "Job created with keyword " + keyword);

        scraperLauncher.runScraperForJob(entity);

        return toModel(entity);
    }

    private void addLog(ScrapeJobEntity job, String level, String message) {
        JobLogEntity log = new JobLogEntity();
        log.setJob(job);
        log.setTs(Instant.now());
        log.setLevel(level);
        log.setMessage(message);
        logs.save(log);
    }

    public List<ScrapeJob> list(int page, int size, String status) {
        var all = jobs.findAll();

        var filtered = all.stream()
                .filter(j -> status == null || status.isBlank() || status.equalsIgnoreCase(j.getStatus()))
                .sorted(Comparator.comparing(ScrapeJobEntity::getCreatedAt).reversed())
                .toList();

        int from = Math.min(page * size, filtered.size());
        int to = Math.min(from + size, filtered.size());

        return filtered.subList(from, to).stream()
                .map(this::toModel)
                .toList();
    }

    public long total(String status) {
        var all = jobs.findAll();
        return all.stream()
                .filter(j -> status == null || status.isBlank() || status.equalsIgnoreCase(j.getStatus()))
                .count();
    }

    public ScrapeJob byId(String id) {
        return jobs.findById(id)
                .map(this::toModel)
                .orElse(null);
    }

    public List<JobLog> logs(String jobId, Instant after) {
        var entries = after == null
                ? logs.findByJob_IdOrderByTsAsc(jobId)
                : logs.findByJob_IdAndTsAfterOrderByTsAsc(jobId, after);

        return entries.stream()
                .map(e -> new JobLog(e.getTs(), e.getLevel(), e.getMessage()))
                .toList();
    }

    private ScrapeJob toModel(ScrapeJobEntity e) {
        return new ScrapeJob(
                e.getId(),
                e.getKeyword(),
                e.getMarket(),
                e.getDepth(),
                e.getSearchUrl(),
                e.getStatus(),
                e.getCreatedAt(),
                e.getStartedAt(),
                e.getEndedAt(),
                e.getError()
        );
    }

    public void saveRawProduct(String jobId, RawProductRequest req) {

        ScrapeJobEntity job = jobs.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown job id " + jobId));

        RawProduct raw = new RawProduct();
        raw.setJobId(job.getId());
        raw.setUrl(req.getUrl());
        raw.setTitle(req.getTitle());
        raw.setPriceRaw(req.getPrice());
        raw.setRatingRaw(req.getRating());
        raw.setReviewsRaw(req.getReviews());
        raw.setAvailability(req.getAvailability());
        raw.setDescriptionBullets(req.getDescriptionBullets());
        raw.setLongDescription(req.getLongDescription());
        raw.setTechnicalDetailsJson(req.getTechnicalDetailsJson());
        raw.setCategoryGuess(req.getCategoryGuess());
        raw.setImageUrl(req.getImageUrl());
        raw.setCreatedAt(Instant.now());
        raw.setProcessed(false);

        rawProducts.save(raw);

        addLog(job, "INFO", "Stored raw product for url " + req.getUrl());
    }
}
