package com.reviewhub.api.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "raw_products")
public class RawProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false, length = 64)
    private String jobId;

    @Column(name = "source_url", length = 2048)
    private String url;

    @Column(name = "title", length = 1000)
    private String title;

    @Column(name = "price_raw", length = 255)
    private String priceRaw;

    @Column(name = "rating_raw", length = 255)
    private String ratingRaw;

    @Column(name = "reviews_raw", length = 255)
    private String reviewsRaw;

    @Column(name = "availability", length = 500)
    private String availability;

    @Lob
    @Column(name = "description_bullets")
    private String descriptionBullets;

    @Lob
    @Column(name = "long_description")
    private String longDescription;

    @Lob
    @Column(name = "technical_details_json")
    private String technicalDetailsJson;

    @Column(name = "category_guess", length = 255)
    private String categoryGuess;

    @Column(name = "image_url", length = 2048)
    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "processed", nullable = false)
    private boolean processed = false;

    public Long getId() {
        return id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriceRaw() {
        return priceRaw;
    }

    public void setPriceRaw(String priceRaw) {
        this.priceRaw = priceRaw;
    }

    public String getRatingRaw() {
        return ratingRaw;
    }

    public void setRatingRaw(String ratingRaw) {
        this.ratingRaw = ratingRaw;
    }

    public String getReviewsRaw() {
        return reviewsRaw;
    }

    public void setReviewsRaw(String reviewsRaw) {
        this.reviewsRaw = reviewsRaw;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescriptionBullets() {
        return descriptionBullets;
    }

    public void setDescriptionBullets(String descriptionBullets) {
        this.descriptionBullets = descriptionBullets;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getTechnicalDetailsJson() {
        return technicalDetailsJson;
    }

    public void setTechnicalDetailsJson(String technicalDetailsJson) {
        this.technicalDetailsJson = technicalDetailsJson;
    }

    public String getCategoryGuess() {
        return categoryGuess;
    }

    public void setCategoryGuess(String categoryGuess) {
        this.categoryGuess = categoryGuess;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
