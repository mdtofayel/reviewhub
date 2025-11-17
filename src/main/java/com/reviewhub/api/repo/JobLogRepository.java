package com.reviewhub.api.repo;

import com.reviewhub.api.model.JobLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface JobLogRepository extends JpaRepository<JobLogEntity, Long> {

    List<JobLogEntity> findByJob_IdOrderByTsAsc(String jobId);

    List<JobLogEntity> findByJob_IdAndTsAfterOrderByTsAsc(String jobId, Instant ts);
}
