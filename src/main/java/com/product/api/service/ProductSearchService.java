package com.product.api.service;

import com.alibaba.fenxiao.crossborder.param.*;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.entity.AuthorizationToken;
import com.alibaba.ocean.rawsdk.common.SDKResult;
import com.product.api.auth.AuthService;
import com.product.api.config.AppConfig;
import com.product.api.factory.ApiRequestFactory;
import com.product.api.param.ImageQueryParam;
import com.product.api.param.ProductDetailParam;
import com.product.api.param.ProductSearchParam;
import com.product.api.util.SecretsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.Callable;

@Service
public class ProductSearchService {
    private static final Logger LOG = LogManager.getLogger();

    private final ApiExecutor apiExecutor;
    private final SecretsManager secretsManager;
    private final AppConfig appConfig;
    private final ApiRequestFactory apiRequestFactory;
    private String accessToken;

    public ProductSearchService(ApiExecutor apiExecutor, SecretsManager secretsManager,
                                AppConfig appConfig, ApiRequestFactory apiRequestFactory) {
        this.apiExecutor = apiExecutor;
        this.secretsManager = secretsManager;
        this.appConfig = appConfig;
        this.apiRequestFactory = apiRequestFactory;
        this.accessToken = secretsManager.getSecret("AccessToken");
    }

    public ProductSearchKeywordQueryResult searchProductsByKeyword(ProductSearchParam productSearchParam) {
        ProductSearchKeywordQueryParam param = apiRequestFactory.createKeywordQueryParam(productSearchParam);
        ProductSearchKeywordQueryResult result = executeWithRetry(() -> apiExecutor.execute(param, accessToken), "product.search.keywordQuery-1");

        if (result != null && result.getResult().getSuccess()) {
            return result;
        } else {
            throw new RuntimeException("API call failed: " + (result != null ? result.getResult().getMessage() : "Unknown error"));
        }
    }

    public ProductSearchQueryProductDetailResult queryProductDetail(ProductDetailParam productDetailParam) {
        ProductSearchQueryProductDetailParam param = apiRequestFactory.createProductDetailParam(productDetailParam);
        ProductSearchQueryProductDetailResult result = executeWithRetry(() -> apiExecutor.execute(param, accessToken), "product.search.queryProductDetail-1");

        if (result != null && result.getResult().getSuccess()) {
            return result;
        } else {
            throw new RuntimeException("API call failed: " + (result != null ? result.getResult().getMessage() : "Unknown error"));
        }
    }

    public ProductImageUploadResult productImageUpload(MultipartFile imageFile) {
        String base64Image;
        try {
            byte[] imageBytes = imageFile.getBytes();
            base64Image = Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert image file to base64", e);
        }

        ProductImageUploadParam param = apiRequestFactory.createImageUploadParam(base64Image);
        ProductImageUploadResult result = executeWithRetry(() -> apiExecutor.execute(param, accessToken), "product.image.upload-1");

        if (result != null && Boolean.TRUE.toString().equals(result.getResult().getSuccess())) {
            return result;
        } else {
            throw new RuntimeException("API call failed: " + (result != null ? result.getResult().getMessage() : "Unknown error"));
        }
    }

    public ProductSearchImageQueryResult imageQuery(ImageQueryParam imageQueryParam) {
        ProductSearchImageQueryParam param = apiRequestFactory.createImageQueryParam(imageQueryParam);
        ProductSearchImageQueryResult result = executeWithRetry(() -> apiExecutor.execute(param, accessToken), "product.search.imageQuery-1");

        if (result != null && Boolean.TRUE.toString().equals(result.getResult().getSuccess())) {
            return result;
        } else {
            throw new RuntimeException("API call failed: " + (result != null ? result.getResult().getMessage() : "Unknown error"));
        }
    }


    private String getAccessToken() {
        String accessToken = AuthService.getAccessToken(
                appConfig.getHost(),
                appConfig.getClientId(),
                secretsManager.getSecret("AppSecret"),
                appConfig.getRedirectUri(),
                secretsManager.getSecret("RefreshToken")
        );
        LOG.info("授权令牌的返回结果：{}", accessToken);
        secretsManager.setSecret("AccessToken", accessToken); // Update the secrets map with the new access token
        return accessToken;
    }

    private String generateAccessToken() {
        AuthorizationToken token = apiExecutor.refreshToken(secretsManager.getSecret("RefreshToken"));
        secretsManager.setSecret("AccessToken", token.getAccess_token()); // Update the secrets map with the new access token
        return token.getAccess_token();
    }


    private <T> T executeWithRetry(Callable<SDKResult<T>> apiCall, String requestUrl) {
        try {
            SDKResult<T> result = apiCall.call();
            LOG.info("API call success for URL{}", requestUrl);
            if (result.getErrorCode() != null && result.getErrorCode().startsWith("4")) {
                LOG.warn("4xx error code received, refreshing access token and retrying...");
                this.accessToken = getAccessToken();
                result = apiCall.call();
            }
            return result.getResult();
        } catch (Exception e) {
            LOG.error("API call failed for URL: {} with error: {}", requestUrl, e.getMessage());
            throw new RuntimeException("API call failed: " + e.getMessage(), e);
        }
    }
}