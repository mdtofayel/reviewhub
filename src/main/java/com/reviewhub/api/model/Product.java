package com.reviewhub.api.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record Product(
		  String id,
		  String slug,
		  String title,
		  String brand,
		  Double price,
		  String currency,
		  String image,
		  Double rating,
		  Integer votes,
		  List<String> badges,
		  Map<String,String> specs,
		  Review review,
		  Seo seo,
		  Instant createdAt,
		  String category
		) {}	