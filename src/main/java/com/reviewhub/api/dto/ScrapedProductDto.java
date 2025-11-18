package com.reviewhub.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ScrapedProductDto {

    @JsonProperty("url")
    private String url;

    @JsonProperty("asin")
    private String asin;

    @JsonProperty("title")
    private String title;

    @JsonProperty("price")
    private String price;

    @JsonProperty("rating_text")
    private String rating;

    @JsonProperty("reviews_text")
    private String reviews;

    @JsonProperty("availability")
    private String availability;

    @JsonProperty("description_bullets")
    private String descriptionBullets;

    @JsonProperty("long_description")
    private String longDescription;

    @JsonProperty("technical_details")
    private Map<String, String> technicalDetails;

    @JsonProperty("image_url")
    private String imageUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
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

    public Map<String, String> getTechnicalDetails() {
        return technicalDetails;
    }

    public void setTechnicalDetails(Map<String, String> technicalDetails) {
        this.technicalDetails = technicalDetails;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
