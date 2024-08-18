package com.product.api.service;

import com.alibaba.fenxiao.crossborder.param.*;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.entity.AuthorizationToken;
import com.product.api.factory.ApiRequestFactory;
import com.product.api.param.ImageQueryParam;
import com.product.api.param.ProductDetailParam;
import com.product.api.param.ProductSearchParam;
import com.product.api.util.ImageCompressor;
import com.product.api.util.RetryUtil;
import com.product.api.util.SecretsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;


/**
 * 商品搜索服务类
 * This class is used to search for products.
 */
@Service
public class ProductSearchService {
    private static final Logger LOG = LogManager.getLogger();

    private static final double ONE_KB = 1024.0;
    private static final float ONE_HUNDRED_KB = 100 * 1024.0f;

    private final ApiExecutor apiExecutor;
    private final SecretsManager secretsManager;
    private final ApiRequestFactory apiRequestFactory;
    private String accessToken;

    public ProductSearchService(ApiExecutor apiExecutor, SecretsManager secretsManager,
                                ApiRequestFactory apiRequestFactory) {
        this.apiExecutor = apiExecutor;
        this.secretsManager = secretsManager;
        this.apiRequestFactory = apiRequestFactory;
        this.accessToken = secretsManager.getSecret("AccessToken");
    }

    public ProductSearchKeywordQueryResult searchProductsByKeyword(ProductSearchParam productSearchParam) {
        ProductSearchKeywordQueryParam param = apiRequestFactory.createKeywordQueryParam(productSearchParam);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    public ProductSearchQueryProductDetailResult queryProductDetail(ProductDetailParam productDetailParam) {
        ProductSearchQueryProductDetailParam param = apiRequestFactory.createProductDetailParam(productDetailParam);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken) , param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    // 线上返回图片失败
    // todo 压缩算法
    // lambda send base64 instead.
    public ProductImageUploadResult productImageUpload(MultipartFile imageFile) {
        String base64Image = "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";
//        try {
//            byte[] imageBytes = imageFile.getBytes();
//            base64Image = Base64.getEncoder().encodeToString(imageBytes);
//            LOG.info("Base64 Image: {}", base64Image); // print the base64 image
//            double sizeBeforeCompressInKB = Base64.getEncoder().encodeToString(imageBytes).length() / ONE_KB;
//
////            base64Image = Base64.getEncoder().encodeToString(imageBytes);
////            double imageBytesSizeInKB = imageBytes.length / ONE_KB;
//
////            float quality = Math.max(0.0f, Math.min(1.0f, ONE_HUNDRED_KB / (float) imageBytes.length));
//            base64Image = ImageCompressor.compressImageToBase64(imageFile, 0.5f);
//
//            // Convert the size to kilobytes (KB)
//            double base64ImageSizeInKB = base64Image.length() / ONE_KB;
//            // Log the size in KB
//            LOG.info("Compressed Image from : {} KB to: {} KB", sizeBeforeCompressInKB, base64ImageSizeInKB);
//
//            // compress the base64 String by 30%
//            if (base64ImageSizeInKB > 100) {
//                double len = base64Image.length() / ONE_KB;
//                base64Image = ImageCompressor.compressBase64String(base64Image);
//                LOG.info("Compressed base64 from : {} KB to: {} KB", len, base64Image.length() / ONE_KB);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to convert image file to base64", e);
//        }

        ProductImageUploadParam param = apiRequestFactory.createImageUploadParam(base64Image);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    public ProductSearchImageQueryResult uploadImageAndQuery(MultipartFile imageFile) {
        // Step 1: Upload the image and get the image ID
        ProductImageUploadResultResultModel uploadResult = productImageUpload(imageFile).getResult();
        ImageQueryParam imageQueryParam = new ImageQueryParam();
        imageQueryParam.setBeginPage(1);
        imageQueryParam.setPageSize(10);
        imageQueryParam.setCountry("en");
        String imageId = uploadResult.getResult();
        // Step 2: Check if the image ID is valid
        if (StringUtils.isNotEmpty(imageId) && !imageId.equals("0")) {
            // Set the image ID to ImageQueryParam
            imageQueryParam.setImageId(imageId);
        } else {
            // Throw an exception to inform the upload image fails
            throw new RuntimeException("Please upload an image less than 100KB");
        }

        // Step 3: Query the image
        return imageQuery(imageQueryParam);
    }

    public ProductSearchImageQueryResult imageQuery(ImageQueryParam imageQueryParam) {
        // todo
        // get image id from image uploading
        ProductSearchImageQueryParam param = apiRequestFactory.createImageQueryParam(imageQueryParam);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    private void refreshAccessToken() {
        AuthorizationToken token = apiExecutor.refreshToken(secretsManager.getSecret("RefreshToken"));
        String newAccessToken = token.getAccess_token();
        secretsManager.setSecret("AccessToken", newAccessToken); // Update the secrets map with the new access token
        this.accessToken = newAccessToken;
    }

//
//    private <T> T executeWithRetry(Callable<SDKResult<T>> apiCall, String requestUrl) {
//        try {
//            SDKResult<T> result = apiCall.call();
//            LOG.info("API call success for URL: {}", requestUrl);
//            if (result.getErrorCode() != null && result.getErrorCode().startsWith("4")) {
//                LOG.warn("4xx error code received, refreshing access token and retrying...");
//                this.accessToken = generateNewAccessToken();
//                LOG.info("API call again: {}, AccessToken refreshed: {}", requestUrl, this.accessToken);
//                result = apiCall.call();
//            }
//            return result.getResult();
//        } catch (Exception e) {
//            LOG.error("API call failed for URL: {} with error: {}", requestUrl, e.getMessage());
//            throw new RuntimeException("API call failed: " + e.getMessage(), e);
//        }
//    }
//    private String getAccessToken() {
//        String accessToken = AuthService.getAccessToken(
//                appConfig.getHost(),
//                appConfig.getClientId(),
//                secretsManager.getSecret("AppSecret"),
//                appConfig.getRedirectUri(),
//                secretsManager.getSecret("RefreshToken")
//        );
//        LOG.info("授权令牌的返回结果：{}", accessToken);
//        secretsManager.setSecret("AccessToken", accessToken); // Update the secrets map with the new access token
//        return accessToken;
//    }
}