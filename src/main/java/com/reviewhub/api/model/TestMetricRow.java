// src/main/java/com/reviewhub/api/model/TestMetricRow.java
package com.reviewhub.api.model;

import java.util.Map;

public record TestMetricRow(
    String label,              // row label, for example "1 hour music streaming (offline)"
    Map<String, String> values // phone name -> value, for example "Xiaomi 14T Pro" -> "6 %"
) {}