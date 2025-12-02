package com.reviewhub.api.admin.controller;

import com.reviewhub.api.dto.NavCategoryDto;
import com.reviewhub.api.model.NavCategory;
import com.reviewhub.api.service.NavCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/nav")
public class NavCategoryController {

    private final NavCategoryService service;

    public NavCategoryController(NavCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<NavCategoryDto> getRootMenu() {
        return service.getMenuTreeRoot();
    }

    @PostMapping
    public NavCategory create(@RequestBody NavCategoryDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public NavCategory update(
            @PathVariable("id") Long id,
            @RequestBody NavCategoryDto dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}

