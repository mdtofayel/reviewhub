package com.reviewhub.api.dto;

public class RawProductRequest {

    private String url;
    private String title;
    private String price;
    private String rating;
    private String reviews;
    private String availability;
    private String descriptionBullets;
    private String longDescription;
    private String technicalDetailsJson;
    private String categoryGuess;
    private String imageUrl;

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
}
