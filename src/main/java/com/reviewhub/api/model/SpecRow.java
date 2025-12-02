package com.reviewhub.api.model;

import java.util.Map;

public record SpecRow(String label,
	    Map<String, String> values) {
	
}
