// src/main/java/com/reviewhub/api/model/DealProduct.java
package com.reviewhub.api.model;

public record DealProduct(
    String id,
    String title,
    String imageUrl,
    String merchant,
    String priceLabel,
    String targetUrl
) {}
