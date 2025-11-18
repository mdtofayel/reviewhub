package com.reviewhub.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewhub.api.config.ScraperProperties;
import com.reviewhub.api.dto.ScrapedProductDto;
import com.reviewhub.api.model.JobLogEntity;
import com.reviewhub.api.model.RawProduct;
import com.reviewhub.api.model.ScrapeJobEntity;
import com.reviewhub.api.repo.JobLogRepository;
import com.reviewhub.api.repo.RawProductRepository;
import com.reviewhub.api.repo.ScrapeJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ScraperLauncherService {

    private static final Logger log = LoggerFactory.getLogger(ScraperLauncherService.class);

    private final ScraperProperties properties;
    private final RawProductRepository rawProductRepository;
    private final ObjectMapper objectMapper;
    private final ScrapeJobRepository jobRepository;
    private final JobLogRepository logRepository;

    public ScraperLauncherService(
            ScraperProperties properties,
            RawProductRepository rawProductRepository,
            ObjectMapper objectMapper,
            ScrapeJobRepository jobRepository,
            JobLogRepository logRepository
    ) {
        this.properties = properties;
        this.rawProductRepository = rawProductRepository;
        this.objectMapper = objectMapper;
        this.jobRepository = jobRepository;
        this.logRepository = logRepository;
    }

    @Async
    @Transactional
    public void runScraperForJob(ScrapeJobEntity jobParam) {
        ScrapeJobEntity job = jobRepository.findById(jobParam.getId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown job " + jobParam.getId()));

        try {
            job.setStatus("RUNNING");
            job.setStartedAt(Instant.now());
            job.setError(null);
            jobRepository.save(job);
            addLog(job, "INFO", "Scraper started");

            int limit = job.getDepth() != null ? job.getDepth() : 10;

            List<String> command = new ArrayList<>();
            command.add(properties.getPythonPath());
            command.add(properties.getScriptPath());
            command.add(job.getId());
            command.add(properties.getBackendBaseUrl());
            command.add(job.getSearchUrl());
            command.add(String.valueOf(limit));

            log.info("Starting scraper for job {} with command: {}", job.getId(), command);

            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);

            Process process = builder.start();

            String outputFileName = null;

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("[scraper {}] {}", job.getId(), line);

                    if (line.contains("products to")) {
                        String[] parts = line.trim().split("\\s+");
                        String last = parts[parts.length - 1];
                        outputFileName = last.replace("'", "").replace("\"", "");
                        log.info("[scraper {}] Detected output file {}", job.getId(), outputFileName);
                    }
                }
            }

            int exitCode = process.waitFor();
            log.info("Scraper finished for job {} with exit code {}", job.getId(), exitCode);

            if (exitCode != 0) {
                job.setStatus("FAILED");
                job.setEndedAt(Instant.now());
                job.setError("Scraper ended with exit code " + exitCode);
                jobRepository.save(job);
                addLog(job, "ERROR", "Scraper failed with exit code " + exitCode);
                return;
            }

            if (outputFileName == null) {
                job.setStatus("FAILED");
                job.setEndedAt(Instant.now());
                job.setError("Scraper did not report an output file");
                jobRepository.save(job);
                addLog(job, "ERROR", "No output file from scraper");
                return;
            }

            importProductsFromFile(job, outputFileName);

            job.setStatus("SUCCEEDED");
            job.setEndedAt(Instant.now());
            job.setError(null);
            jobRepository.save(job);
            addLog(job, "INFO", "Job succeeded and products imported");

        } catch (Exception e) {
            log.error("Failed to start or handle scraper for job {}", jobParam.getId(), e);
            job.setStatus("FAILED");
            job.setEndedAt(Instant.now());
            job.setError(e.getMessage());
            jobRepository.save(job);
            addLog(job, "ERROR", "Exception during scraping " + e.getMessage());
        }
    }

    private void importProductsFromFile(ScrapeJobEntity job, String outputFileName) {
        try {
            Path path = Paths.get(outputFileName);
            if (!Files.exists(path)) {
                log.error("Output file {} for job {} does not exist", outputFileName, job.getId());
                addLog(job, "ERROR", "Output file does not exist: " + outputFileName);
                return;
            }

            String json = Files.readString(path, StandardCharsets.UTF_8);

            List<ScrapedProductDto> products = objectMapper.readValue(
                    json,
                    new TypeReference<List<ScrapedProductDto>>() {}
            );

            List<RawProduct> entities = new ArrayList<>();
            int index = 1;

            for (ScrapedProductDto p : products) {
                RawProduct e = new RawProduct();
                e.setJobId(job.getId());
                e.setUrl(p.getUrl());
                e.setTitle(p.getTitle());
                e.setPriceRaw(p.getPrice());
                e.setRatingRaw(p.getRating());
                e.setReviewsRaw(p.getReviews());
                e.setAvailability(p.getAvailability());
                e.setDescriptionBullets(p.getDescriptionBullets());
                e.setLongDescription(p.getLongDescription());

                Map<String, String> tech = p.getTechnicalDetails();
                if (tech != null && !tech.isEmpty()) {
                    String techJson = objectMapper.writeValueAsString(tech);
                    e.setTechnicalDetailsJson(techJson);
                }

                String localImagePath = saveImageIfPresent(p.getImageUrl(), job.getId(), index);
                e.setImageUrl(localImagePath);

                e.setCategoryGuess(job.getSearchUrl());

                entities.add(e);
                index++;
            }

            rawProductRepository.saveAll(entities);
            log.info("Imported {} products into raw_products for job {}", entities.size(), job.getId());
            addLog(job, "INFO", "Imported " + entities.size() + " products");

        } catch (Exception e) {
            log.error("Failed to import products for job {} from file {}", job.getId(), outputFileName, e);
            addLog(job, "ERROR", "Failed to import products: " + e.getMessage());
            throw new RuntimeException("Import failed", e);
        }
    }

    private String saveImageIfPresent(String imageUrl, String jobId, int productIndex) {
        try {
            if (imageUrl == null || imageUrl.isBlank()) {
                return null;
            }

            Path baseDir = Paths.get("productImages");
            String jobFragment = jobId.length() > 8 ? jobId.substring(0, 8) : jobId;
            Path jobDir = baseDir.resolve(jobFragment);
            Files.createDirectories(jobDir);

            String fileNameFromUrl = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            String extension = "jpg";
            int dotIndex = fileNameFromUrl.lastIndexOf('.');
            if (dotIndex != -1 && dotIndex < fileNameFromUrl.length() - 1) {
                extension = fileNameFromUrl.substring(dotIndex + 1);
            }

            String newFileName = "product_" + productIndex + "." + extension;
            Path targetPath = jobDir.resolve(newFileName);

            URL url = new URL(imageUrl);
            try (InputStream in = url.openStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return targetPath.toString();

        } catch (Exception ex) {
            log.warn("Failed to download image {} for job {}, product {}: {}",
                    imageUrl, jobId, productIndex, ex.getMessage());
            return null;
        }
    }

    private void addLog(ScrapeJobEntity job, String level, String message) {
        JobLogEntity entry = new JobLogEntity();
        entry.setJob(job);
        entry.setTs(Instant.now());
        entry.setLevel(level);
        entry.setMessage(message);
        logRepository.save(entry);
    }
}
