package com.product.api.config;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIClientConfig {

    @Value("${api.appKey}")
    private String appKey;

    @Value("${api.secKey}")
    private String secKey;

    @Bean
    public ApiExecutor apiExecutor() {
        return new ApiExecutor(appKey, secKey);
    }
}