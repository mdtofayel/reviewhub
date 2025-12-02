package com.reviewhub.api.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewhub.api.model.NavCategory;

public interface NavCategoryRepository extends JpaRepository<NavCategory, Long> {
    List<NavCategory> findAllByParentIsNullOrderByOrderIndexAsc();
    List<NavCategory> findAllByParentIdOrderByOrderIndexAsc(Long parentId);
}
