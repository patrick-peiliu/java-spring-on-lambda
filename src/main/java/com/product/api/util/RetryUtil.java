package com.product.api.util;

import com.alibaba.ocean.rawsdk.common.SDKResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

public class RetryUtil {
    private static final Logger LOG = LogManager.getLogger();

    public static <T> T executeWithRetry(Callable<SDKResult<T>> apiCall, String requestUrl, Runnable refreshToken) {
        try {
            SDKResult<T> result = apiCall.call();
            LOG.info("API call success for URL: {}", requestUrl);
            if (result.getErrorCode() != null && result.getErrorCode().startsWith("4")) {
                LOG.warn("4xx error code received, refreshing access token and retrying...");
                refreshToken.run();
                result = apiCall.call();
            }
            return result.getResult();
        } catch (Exception e) {
            LOG.error("API call failed for URL: {} with error: {}", requestUrl, e.getMessage());
            throw new RuntimeException("API call failed: " + e.getMessage(), e);
        }
    }
}