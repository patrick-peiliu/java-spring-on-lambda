package com.product.api.config;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIClientConfig {

    @Bean
    public ApiExecutor apiExecutor() {
        String appKey = "yourAppKey"; // Replace with your actual appKey
        String secKey = "yourSecKey"; // Replace with your actual secKey
        return new ApiExecutor(appKey, secKey);
    }
}