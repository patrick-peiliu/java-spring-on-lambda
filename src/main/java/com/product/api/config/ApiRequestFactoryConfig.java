package com.product.api.config;

import com.product.api.factory.ApiRequestFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiRequestFactoryConfig {

    @Bean
    public ApiRequestFactory apiRequestFactory() {
        return new ApiRequestFactory();
    }
}