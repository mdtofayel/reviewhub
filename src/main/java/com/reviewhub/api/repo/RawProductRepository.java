package com.reviewhub.api.repo;

import com.reviewhub.api.model.RawProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawProductRepository extends JpaRepository<RawProduct, Long> {

    List<RawProduct> findByJobIdAndProcessedFalse(String jobId);

    List<RawProduct> findByJobIdOrderByCreatedAtAsc(String jobId);
}
