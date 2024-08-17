package com.product.api.service;

import com.alibaba.fenxiao.crossborder.param.*;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.entity.AuthorizationToken;
import com.product.api.factory.ApiRequestFactory;
import com.product.api.param.ImageQueryParam;
import com.product.api.param.ProductDetailParam;
import com.product.api.param.ProductSearchParam;
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
    public ProductImageUploadResult productImageUpload(MultipartFile imageFile) {
        String base64Image;
        try {
            byte[] imageBytes = imageFile.getBytes();
            base64Image = Base64.getEncoder().encodeToString(imageBytes);
//            LOG.info("Base64 Image: {}", base64Image); // print the base64 image
            // Calculate the size of the base64 string in bytes
            int base64ImageSizeInBytes = base64Image.length();

            // Convert the size to kilobytes (KB)
            double base64ImageSizeInKB = base64ImageSizeInBytes / 1024.0;

            // Log the size in KB
            LOG.info("Base64 Image Size: {} KB", base64ImageSizeInKB);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert image file to base64", e);
        }

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
            throw new RuntimeException("Image upload failed: Invalid image ID");
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