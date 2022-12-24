package com.binarair.binarairrestapi.security;

import com.google.cloud.storage.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/**")
                        .allowedMethods(HttpMethod.HEAD.name(), HttpMethod.GET.name(), HttpMethod.PUT.name(),
                                HttpMethod.POST.name(), HttpMethod.DELETE.name());
            }
        };
    }
}
