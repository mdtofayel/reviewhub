package com.reviewhub.api.dto;

import java.util.List;

import jakarta.persistence.Column;

public class NavCategoryDto {

    private Long id;
    private String name;
    private String slug;
    private String icon;
    private Boolean visible;
    private Integer orderIndex;
    private Long parentId;
	private String path;
    private List<NavCategoryDto> children;   // new

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public List<NavCategoryDto> getChildren() { return children; }
    public void setChildren(List<NavCategoryDto> children) { this.children = children; }
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
