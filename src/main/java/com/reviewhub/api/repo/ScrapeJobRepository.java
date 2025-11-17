package com.reviewhub.api.repo;

import com.reviewhub.api.model.ScrapeJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapeJobRepository extends JpaRepository<ScrapeJobEntity, String> {
}
