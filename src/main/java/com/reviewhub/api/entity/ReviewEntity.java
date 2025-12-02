// src/main/java/com/reviewhub/api/entity/ProductEntity.java
package com.reviewhub.api.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "products")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(length = 200)
    private String brand;

    private Double price;

    @Column(length = 10)
    private String currency;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    private Double rating;

    @Column(name = "review_count")
    private Integer reviewCount;

    /** Comma separated labels like "Editors choice,Great value" for now */
    @Column(name = "tags", length = 1000)
    private String tags;

    /** Short key value attributes such as Weight, Color etc, stored as JSON */
    @Column(name = "attributes_json", columnDefinition = "jsonb")
    private String attributesJson;

    /** Nested review object stored as JSON */
    @Column(name = "review_json", columnDefinition = "jsonb")
    private String reviewJson;

    /** SEO info as JSON */
    @Column(name = "seo_json", columnDefinition = "jsonb")
    private String seoJson;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "primary_category", length = 100)
    private String primaryCategory;
    
    @Column(name = "review_body_json", columnDefinition = "jsonb")
    private String reviewBodyJson;

    @Column(name = "gallery_images_json", columnDefinition = "jsonb")
    private String galleryImagesJson;  
    // getters and setters

    public String getReviewBodyJson() {
        return reviewBodyJson;
    }

    public void setReviewBodyJson(String reviewBodyJson) {
        this.reviewBodyJson = reviewBodyJson;
    }
    
    
     

    public String getGalleryImagesJson() {
        return galleryImagesJson;
    }

    public void setGalleryImagesJson(String galleryImagesJson) {
        this.galleryImagesJson = galleryImagesJson;
    }
    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAttributesJson() {
        return attributesJson;
    }

    public void setAttributesJson(String attributesJson) {
        this.attributesJson = attributesJson;
    }

    public String getReviewJson() {
        return reviewJson;
    }

    public void setReviewJson(String reviewJson) {
        this.reviewJson = reviewJson;
    }

    public String getSeoJson() {
        return seoJson;
    }

    public void setSeoJson(String seoJson) {
        this.seoJson = seoJson;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }
}
