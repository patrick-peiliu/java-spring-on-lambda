package com.product.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${app.host}")
    private String host;

    @Value("${api.appKey}")
    private String clientId;

    @Value("${app.redirectUri}")
    private String redirectUri;

    public String getHost() {
        return host;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}