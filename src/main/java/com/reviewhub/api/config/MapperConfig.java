// src/main/java/com/reviewhub/api/config/MapperConfig.java
package com.reviewhub.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewhub.api.mapper.ReviewMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class MapperConfig {

 @Bean
 public ReviewMapper reviewMapper(ObjectMapper objectMapper) {
     return new ReviewMapper(objectMapper);
 }
}
