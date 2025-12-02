package com.reviewhub.api.service;

import com.reviewhub.api.dto.NavCategoryDto;
import com.reviewhub.api.model.NavCategory;
import com.reviewhub.api.repo.NavCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavCategoryService {

    private final NavCategoryRepository repo;

    public NavCategoryService(NavCategoryRepository repo) {
        this.repo = repo;
    }

    // root menu as tree
    public List<NavCategoryDto> getMenuTreeRoot() {
        List<NavCategory> roots =
                repo.findAllByParentIsNullOrderByOrderIndexAsc();

        return roots.stream()
                .map(this::toDtoTree)
                .toList();
    }

    public NavCategory create(NavCategoryDto dto) {
        NavCategory cat = new NavCategory();
        applyDto(cat, dto);
        return repo.save(cat);
    }

    public NavCategory update(Long id, NavCategoryDto dto) {
        NavCategory cat = repo.findById(id).orElseThrow();
        applyDto(cat, dto);
        return repo.save(cat);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    private void applyDto(NavCategory cat, NavCategoryDto dto) {
        cat.setName(dto.getName());
        cat.setSlug(dto.getSlug());
        cat.setIcon(dto.getIcon());
        cat.setPath(dto.getPath());


        Boolean visible = dto.getVisible();
        cat.setVisible(visible != null ? visible : true);

        Integer orderIndex = dto.getOrderIndex();
        cat.setOrderIndex(orderIndex != null ? orderIndex : 0);

        Long parentId = dto.getParentId();
        if (parentId != null && cat.getId() != null && parentId.equals(cat.getId())) {
            parentId = null; // avoid self parent
        }

        if (parentId != null) {
            NavCategory parent = repo.findById(parentId).orElse(null);
            cat.setParent(parent);
        } else {
            cat.setParent(null);
        }
    }

    private NavCategoryDto toDtoTree(NavCategory cat) {
        NavCategoryDto dto = new NavCategoryDto();
        dto.setId(cat.getId());
        dto.setName(cat.getName());
        dto.setSlug(cat.getSlug());
        dto.setIcon(cat.getIcon());
        dto.setVisible(cat.isVisible());
        dto.setOrderIndex(cat.getOrderIndex());
        if (cat.getParent() != null) {
            dto.setParentId(cat.getParent().getId());
        }

        // load children for this node
        List<NavCategory> children =
                repo.findAllByParentIdOrderByOrderIndexAsc(cat.getId());

        List<NavCategoryDto> childDtos = children.stream()
                .map(this::toDtoTree)
                .toList();

        dto.setChildren(childDtos);
        return dto;
    }
}
