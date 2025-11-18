package com.reviewhub.api.utilities;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // productImages folder in backend project root
        Path imageDir = Paths.get("productImages").toAbsolutePath().normalize();

        String imageLocation = "file:" + imageDir.toString() + "/";

        registry.addResourceHandler("/productImages/**")
                .addResourceLocations(imageLocation);
    }
}
