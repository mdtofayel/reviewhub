package com.reviewhub.api.service;

import com.reviewhub.api.config.ScraperProperties;
import com.reviewhub.api.model.ScrapeJobEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperLauncherService {

    private static final Logger log = LoggerFactory.getLogger(ScraperLauncherService.class);

    private final ScraperProperties properties;

    public ScraperLauncherService(ScraperProperties properties) {
        this.properties = properties;
    }
    
    @Async
    public void runScraperForJob(ScrapeJobEntity job) {
        try {
            int limit = job.getDepth() != null ? job.getDepth() : 10;

            List<String> command = new ArrayList<>();
            command.add(properties.getPythonPath());
            command.add(properties.getScriptPath());
            command.add(job.getId());
            command.add(properties.getBackendBaseUrl());
            command.add(job.getSearchUrl());
            command.add(String.valueOf(limit));

            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);

            Process process = builder.start();

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("[scraper {}] {}", job.getId(), line);
                }
            }

            int exitCode = process.waitFor();
            log.info("Scraper finished for job {} with exit code {}", job.getId(), exitCode);
        } catch (Exception e) {
            log.error("Failed to start scraper for job {}", job.getId(), e);
        }
    }

}
