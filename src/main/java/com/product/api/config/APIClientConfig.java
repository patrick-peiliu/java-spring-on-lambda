package com.product.api.config;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.product.api.util.SecretsManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIClientConfig {

    @Value("${api.appKey}")
    private String appKey;


    private final SecretsManager secretsManager;

    public APIClientConfig(SecretsManager secretsManager) {
        this.secretsManager = secretsManager;
    }

    @Bean
    public ApiExecutor apiExecutor() {
        return new ApiExecutor(appKey, secretsManager.getSecret("AppSecret"));
    }
}