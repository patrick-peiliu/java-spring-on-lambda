package com.product.api.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SecretsManager {
    private final Map<String, String> secrets;

    public SecretsManager(Map<String, String> secrets) {
        this.secrets = new ConcurrentHashMap<>(secrets); // Ensure thread safety
    }

    public String getSecret(String key) {
        return secrets.get(key);
    }

    public void setSecret(String key, String value) {
        secrets.put(key, value);
    }
}