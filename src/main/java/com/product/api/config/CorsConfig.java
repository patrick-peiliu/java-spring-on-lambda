package com.product.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("http://127.0.0.1:5500"); // Allow specific test domain
        corsConfiguration.addAllowedOriginPattern("http://192.168.68.74:5500"); // Allow specific test domain
        corsConfiguration.addAllowedOriginPattern("*epicsourcing*"); // Allow any domain containing "epicsourcing"
        corsConfiguration.addAllowedOriginPattern("*.webflow.io"); // Allow any test domain of Webflow
        corsConfiguration.addAllowedMethod("*"); // Allow all methods (GET, POST, etc.)
        corsConfiguration.addAllowedHeader("*"); // Allow all headers
        corsConfiguration.setAllowCredentials(true); // Allow credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Apply CORS settings to all endpoints

        return new CorsFilter(source);
    }
}